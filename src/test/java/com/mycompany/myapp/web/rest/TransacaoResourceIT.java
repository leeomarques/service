package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Categoria;
import com.mycompany.myapp.domain.Orcamento;
import com.mycompany.myapp.domain.Transacao;
import com.mycompany.myapp.repository.TransacaoRepository;
import com.mycompany.myapp.service.dto.TransacaoDTO;
import com.mycompany.myapp.service.mapper.TransacaoMapper;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link TransacaoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TransacaoResourceIT {

    private static final Double DEFAULT_VALOR = 1D;
    private static final Double UPDATED_VALOR = 2D;
    private static final Double SMALLER_VALOR = 1D - 1D;

    private static final String ENTITY_API_URL = "/api/transacaos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TransacaoRepository transacaoRepository;

    @Autowired
    private TransacaoMapper transacaoMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTransacaoMockMvc;

    private Transacao transacao;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Transacao createEntity(EntityManager em) {
        Transacao transacao = new Transacao().valor(DEFAULT_VALOR);
        return transacao;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Transacao createUpdatedEntity(EntityManager em) {
        Transacao transacao = new Transacao().valor(UPDATED_VALOR);
        return transacao;
    }

    @BeforeEach
    public void initTest() {
        transacao = createEntity(em);
    }

    @Test
    @Transactional
    void createTransacao() throws Exception {
        int databaseSizeBeforeCreate = transacaoRepository.findAll().size();
        // Create the Transacao
        TransacaoDTO transacaoDTO = transacaoMapper.toDto(transacao);
        restTransacaoMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(transacaoDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Transacao in the database
        List<Transacao> transacaoList = transacaoRepository.findAll();
        assertThat(transacaoList).hasSize(databaseSizeBeforeCreate + 1);
        Transacao testTransacao = transacaoList.get(transacaoList.size() - 1);
        assertThat(testTransacao.getValor()).isEqualTo(DEFAULT_VALOR);
    }

    @Test
    @Transactional
    void createTransacaoWithExistingId() throws Exception {
        // Create the Transacao with an existing ID
        transacao.setId(1L);
        TransacaoDTO transacaoDTO = transacaoMapper.toDto(transacao);

        int databaseSizeBeforeCreate = transacaoRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTransacaoMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(transacaoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Transacao in the database
        List<Transacao> transacaoList = transacaoRepository.findAll();
        assertThat(transacaoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkValorIsRequired() throws Exception {
        int databaseSizeBeforeTest = transacaoRepository.findAll().size();
        // set the field null
        transacao.setValor(null);

        // Create the Transacao, which fails.
        TransacaoDTO transacaoDTO = transacaoMapper.toDto(transacao);

        restTransacaoMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(transacaoDTO))
            )
            .andExpect(status().isBadRequest());

        List<Transacao> transacaoList = transacaoRepository.findAll();
        assertThat(transacaoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTransacaos() throws Exception {
        // Initialize the database
        transacaoRepository.saveAndFlush(transacao);

        // Get all the transacaoList
        restTransacaoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(transacao.getId().intValue())))
            .andExpect(jsonPath("$.[*].valor").value(hasItem(DEFAULT_VALOR.doubleValue())));
    }

    @Test
    @Transactional
    void getTransacao() throws Exception {
        // Initialize the database
        transacaoRepository.saveAndFlush(transacao);

        // Get the transacao
        restTransacaoMockMvc
            .perform(get(ENTITY_API_URL_ID, transacao.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(transacao.getId().intValue()))
            .andExpect(jsonPath("$.valor").value(DEFAULT_VALOR.doubleValue()));
    }

    @Test
    @Transactional
    void getTransacaosByIdFiltering() throws Exception {
        // Initialize the database
        transacaoRepository.saveAndFlush(transacao);

        Long id = transacao.getId();

        defaultTransacaoShouldBeFound("id.equals=" + id);
        defaultTransacaoShouldNotBeFound("id.notEquals=" + id);

        defaultTransacaoShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultTransacaoShouldNotBeFound("id.greaterThan=" + id);

        defaultTransacaoShouldBeFound("id.lessThanOrEqual=" + id);
        defaultTransacaoShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllTransacaosByValorIsEqualToSomething() throws Exception {
        // Initialize the database
        transacaoRepository.saveAndFlush(transacao);

        // Get all the transacaoList where valor equals to DEFAULT_VALOR
        defaultTransacaoShouldBeFound("valor.equals=" + DEFAULT_VALOR);

        // Get all the transacaoList where valor equals to UPDATED_VALOR
        defaultTransacaoShouldNotBeFound("valor.equals=" + UPDATED_VALOR);
    }

    @Test
    @Transactional
    void getAllTransacaosByValorIsInShouldWork() throws Exception {
        // Initialize the database
        transacaoRepository.saveAndFlush(transacao);

        // Get all the transacaoList where valor in DEFAULT_VALOR or UPDATED_VALOR
        defaultTransacaoShouldBeFound("valor.in=" + DEFAULT_VALOR + "," + UPDATED_VALOR);

        // Get all the transacaoList where valor equals to UPDATED_VALOR
        defaultTransacaoShouldNotBeFound("valor.in=" + UPDATED_VALOR);
    }

    @Test
    @Transactional
    void getAllTransacaosByValorIsNullOrNotNull() throws Exception {
        // Initialize the database
        transacaoRepository.saveAndFlush(transacao);

        // Get all the transacaoList where valor is not null
        defaultTransacaoShouldBeFound("valor.specified=true");

        // Get all the transacaoList where valor is null
        defaultTransacaoShouldNotBeFound("valor.specified=false");
    }

    @Test
    @Transactional
    void getAllTransacaosByValorIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        transacaoRepository.saveAndFlush(transacao);

        // Get all the transacaoList where valor is greater than or equal to DEFAULT_VALOR
        defaultTransacaoShouldBeFound("valor.greaterThanOrEqual=" + DEFAULT_VALOR);

        // Get all the transacaoList where valor is greater than or equal to UPDATED_VALOR
        defaultTransacaoShouldNotBeFound("valor.greaterThanOrEqual=" + UPDATED_VALOR);
    }

    @Test
    @Transactional
    void getAllTransacaosByValorIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        transacaoRepository.saveAndFlush(transacao);

        // Get all the transacaoList where valor is less than or equal to DEFAULT_VALOR
        defaultTransacaoShouldBeFound("valor.lessThanOrEqual=" + DEFAULT_VALOR);

        // Get all the transacaoList where valor is less than or equal to SMALLER_VALOR
        defaultTransacaoShouldNotBeFound("valor.lessThanOrEqual=" + SMALLER_VALOR);
    }

    @Test
    @Transactional
    void getAllTransacaosByValorIsLessThanSomething() throws Exception {
        // Initialize the database
        transacaoRepository.saveAndFlush(transacao);

        // Get all the transacaoList where valor is less than DEFAULT_VALOR
        defaultTransacaoShouldNotBeFound("valor.lessThan=" + DEFAULT_VALOR);

        // Get all the transacaoList where valor is less than UPDATED_VALOR
        defaultTransacaoShouldBeFound("valor.lessThan=" + UPDATED_VALOR);
    }

    @Test
    @Transactional
    void getAllTransacaosByValorIsGreaterThanSomething() throws Exception {
        // Initialize the database
        transacaoRepository.saveAndFlush(transacao);

        // Get all the transacaoList where valor is greater than DEFAULT_VALOR
        defaultTransacaoShouldNotBeFound("valor.greaterThan=" + DEFAULT_VALOR);

        // Get all the transacaoList where valor is greater than SMALLER_VALOR
        defaultTransacaoShouldBeFound("valor.greaterThan=" + SMALLER_VALOR);
    }

    @Test
    @Transactional
    void getAllTransacaosByOrcamentoIsEqualToSomething() throws Exception {
        Orcamento orcamento;
        if (TestUtil.findAll(em, Orcamento.class).isEmpty()) {
            transacaoRepository.saveAndFlush(transacao);
            orcamento = OrcamentoResourceIT.createEntity(em);
        } else {
            orcamento = TestUtil.findAll(em, Orcamento.class).get(0);
        }
        em.persist(orcamento);
        em.flush();
        transacao.setOrcamento(orcamento);
        transacaoRepository.saveAndFlush(transacao);
        Long orcamentoId = orcamento.getId();
        // Get all the transacaoList where orcamento equals to orcamentoId
        defaultTransacaoShouldBeFound("orcamentoId.equals=" + orcamentoId);

        // Get all the transacaoList where orcamento equals to (orcamentoId + 1)
        defaultTransacaoShouldNotBeFound("orcamentoId.equals=" + (orcamentoId + 1));
    }

    @Test
    @Transactional
    void getAllTransacaosByCategoriaIsEqualToSomething() throws Exception {
        Categoria categoria;
        if (TestUtil.findAll(em, Categoria.class).isEmpty()) {
            transacaoRepository.saveAndFlush(transacao);
            categoria = CategoriaResourceIT.createEntity(em);
        } else {
            categoria = TestUtil.findAll(em, Categoria.class).get(0);
        }
        em.persist(categoria);
        em.flush();
        transacao.setCategoria(categoria);
        transacaoRepository.saveAndFlush(transacao);
        Long categoriaId = categoria.getId();
        // Get all the transacaoList where categoria equals to categoriaId
        defaultTransacaoShouldBeFound("categoriaId.equals=" + categoriaId);

        // Get all the transacaoList where categoria equals to (categoriaId + 1)
        defaultTransacaoShouldNotBeFound("categoriaId.equals=" + (categoriaId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTransacaoShouldBeFound(String filter) throws Exception {
        restTransacaoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(transacao.getId().intValue())))
            .andExpect(jsonPath("$.[*].valor").value(hasItem(DEFAULT_VALOR.doubleValue())));

        // Check, that the count call also returns 1
        restTransacaoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTransacaoShouldNotBeFound(String filter) throws Exception {
        restTransacaoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTransacaoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingTransacao() throws Exception {
        // Get the transacao
        restTransacaoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTransacao() throws Exception {
        // Initialize the database
        transacaoRepository.saveAndFlush(transacao);

        int databaseSizeBeforeUpdate = transacaoRepository.findAll().size();

        // Update the transacao
        Transacao updatedTransacao = transacaoRepository.findById(transacao.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedTransacao are not directly saved in db
        em.detach(updatedTransacao);
        updatedTransacao.valor(UPDATED_VALOR);
        TransacaoDTO transacaoDTO = transacaoMapper.toDto(updatedTransacao);

        restTransacaoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, transacaoDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(transacaoDTO))
            )
            .andExpect(status().isOk());

        // Validate the Transacao in the database
        List<Transacao> transacaoList = transacaoRepository.findAll();
        assertThat(transacaoList).hasSize(databaseSizeBeforeUpdate);
        Transacao testTransacao = transacaoList.get(transacaoList.size() - 1);
        assertThat(testTransacao.getValor()).isEqualTo(UPDATED_VALOR);
    }

    @Test
    @Transactional
    void putNonExistingTransacao() throws Exception {
        int databaseSizeBeforeUpdate = transacaoRepository.findAll().size();
        transacao.setId(longCount.incrementAndGet());

        // Create the Transacao
        TransacaoDTO transacaoDTO = transacaoMapper.toDto(transacao);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTransacaoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, transacaoDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(transacaoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Transacao in the database
        List<Transacao> transacaoList = transacaoRepository.findAll();
        assertThat(transacaoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTransacao() throws Exception {
        int databaseSizeBeforeUpdate = transacaoRepository.findAll().size();
        transacao.setId(longCount.incrementAndGet());

        // Create the Transacao
        TransacaoDTO transacaoDTO = transacaoMapper.toDto(transacao);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTransacaoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(transacaoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Transacao in the database
        List<Transacao> transacaoList = transacaoRepository.findAll();
        assertThat(transacaoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTransacao() throws Exception {
        int databaseSizeBeforeUpdate = transacaoRepository.findAll().size();
        transacao.setId(longCount.incrementAndGet());

        // Create the Transacao
        TransacaoDTO transacaoDTO = transacaoMapper.toDto(transacao);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTransacaoMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(transacaoDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Transacao in the database
        List<Transacao> transacaoList = transacaoRepository.findAll();
        assertThat(transacaoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTransacaoWithPatch() throws Exception {
        // Initialize the database
        transacaoRepository.saveAndFlush(transacao);

        int databaseSizeBeforeUpdate = transacaoRepository.findAll().size();

        // Update the transacao using partial update
        Transacao partialUpdatedTransacao = new Transacao();
        partialUpdatedTransacao.setId(transacao.getId());

        restTransacaoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTransacao.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTransacao))
            )
            .andExpect(status().isOk());

        // Validate the Transacao in the database
        List<Transacao> transacaoList = transacaoRepository.findAll();
        assertThat(transacaoList).hasSize(databaseSizeBeforeUpdate);
        Transacao testTransacao = transacaoList.get(transacaoList.size() - 1);
        assertThat(testTransacao.getValor()).isEqualTo(DEFAULT_VALOR);
    }

    @Test
    @Transactional
    void fullUpdateTransacaoWithPatch() throws Exception {
        // Initialize the database
        transacaoRepository.saveAndFlush(transacao);

        int databaseSizeBeforeUpdate = transacaoRepository.findAll().size();

        // Update the transacao using partial update
        Transacao partialUpdatedTransacao = new Transacao();
        partialUpdatedTransacao.setId(transacao.getId());

        partialUpdatedTransacao.valor(UPDATED_VALOR);

        restTransacaoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTransacao.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTransacao))
            )
            .andExpect(status().isOk());

        // Validate the Transacao in the database
        List<Transacao> transacaoList = transacaoRepository.findAll();
        assertThat(transacaoList).hasSize(databaseSizeBeforeUpdate);
        Transacao testTransacao = transacaoList.get(transacaoList.size() - 1);
        assertThat(testTransacao.getValor()).isEqualTo(UPDATED_VALOR);
    }

    @Test
    @Transactional
    void patchNonExistingTransacao() throws Exception {
        int databaseSizeBeforeUpdate = transacaoRepository.findAll().size();
        transacao.setId(longCount.incrementAndGet());

        // Create the Transacao
        TransacaoDTO transacaoDTO = transacaoMapper.toDto(transacao);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTransacaoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, transacaoDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(transacaoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Transacao in the database
        List<Transacao> transacaoList = transacaoRepository.findAll();
        assertThat(transacaoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTransacao() throws Exception {
        int databaseSizeBeforeUpdate = transacaoRepository.findAll().size();
        transacao.setId(longCount.incrementAndGet());

        // Create the Transacao
        TransacaoDTO transacaoDTO = transacaoMapper.toDto(transacao);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTransacaoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(transacaoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Transacao in the database
        List<Transacao> transacaoList = transacaoRepository.findAll();
        assertThat(transacaoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTransacao() throws Exception {
        int databaseSizeBeforeUpdate = transacaoRepository.findAll().size();
        transacao.setId(longCount.incrementAndGet());

        // Create the Transacao
        TransacaoDTO transacaoDTO = transacaoMapper.toDto(transacao);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTransacaoMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(transacaoDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Transacao in the database
        List<Transacao> transacaoList = transacaoRepository.findAll();
        assertThat(transacaoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTransacao() throws Exception {
        // Initialize the database
        transacaoRepository.saveAndFlush(transacao);

        int databaseSizeBeforeDelete = transacaoRepository.findAll().size();

        // Delete the transacao
        restTransacaoMockMvc
            .perform(delete(ENTITY_API_URL_ID, transacao.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Transacao> transacaoList = transacaoRepository.findAll();
        assertThat(transacaoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
