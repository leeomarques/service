package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Orcamento;
import com.mycompany.myapp.domain.Transacao;
import com.mycompany.myapp.repository.OrcamentoRepository;
import com.mycompany.myapp.service.dto.OrcamentoDTO;
import com.mycompany.myapp.service.mapper.OrcamentoMapper;
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
 * Integration tests for the {@link OrcamentoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class OrcamentoResourceIT {

    private static final Integer DEFAULT_ANO = 1;
    private static final Integer UPDATED_ANO = 2;
    private static final Integer SMALLER_ANO = 1 - 1;

    private static final String DEFAULT_MES = "AAAAAAAAAA";
    private static final String UPDATED_MES = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/orcamentos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private OrcamentoRepository orcamentoRepository;

    @Autowired
    private OrcamentoMapper orcamentoMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOrcamentoMockMvc;

    private Orcamento orcamento;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Orcamento createEntity(EntityManager em) {
        Orcamento orcamento = new Orcamento().ano(DEFAULT_ANO).mes(DEFAULT_MES);
        return orcamento;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Orcamento createUpdatedEntity(EntityManager em) {
        Orcamento orcamento = new Orcamento().ano(UPDATED_ANO).mes(UPDATED_MES);
        return orcamento;
    }

    @BeforeEach
    public void initTest() {
        orcamento = createEntity(em);
    }

    @Test
    @Transactional
    void createOrcamento() throws Exception {
        int databaseSizeBeforeCreate = orcamentoRepository.findAll().size();
        // Create the Orcamento
        OrcamentoDTO orcamentoDTO = orcamentoMapper.toDto(orcamento);
        restOrcamentoMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(orcamentoDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Orcamento in the database
        List<Orcamento> orcamentoList = orcamentoRepository.findAll();
        assertThat(orcamentoList).hasSize(databaseSizeBeforeCreate + 1);
        Orcamento testOrcamento = orcamentoList.get(orcamentoList.size() - 1);
        assertThat(testOrcamento.getAno()).isEqualTo(DEFAULT_ANO);
        assertThat(testOrcamento.getMes()).isEqualTo(DEFAULT_MES);
    }

    @Test
    @Transactional
    void createOrcamentoWithExistingId() throws Exception {
        // Create the Orcamento with an existing ID
        orcamento.setId(1L);
        OrcamentoDTO orcamentoDTO = orcamentoMapper.toDto(orcamento);

        int databaseSizeBeforeCreate = orcamentoRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOrcamentoMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(orcamentoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Orcamento in the database
        List<Orcamento> orcamentoList = orcamentoRepository.findAll();
        assertThat(orcamentoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkAnoIsRequired() throws Exception {
        int databaseSizeBeforeTest = orcamentoRepository.findAll().size();
        // set the field null
        orcamento.setAno(null);

        // Create the Orcamento, which fails.
        OrcamentoDTO orcamentoDTO = orcamentoMapper.toDto(orcamento);

        restOrcamentoMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(orcamentoDTO))
            )
            .andExpect(status().isBadRequest());

        List<Orcamento> orcamentoList = orcamentoRepository.findAll();
        assertThat(orcamentoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMesIsRequired() throws Exception {
        int databaseSizeBeforeTest = orcamentoRepository.findAll().size();
        // set the field null
        orcamento.setMes(null);

        // Create the Orcamento, which fails.
        OrcamentoDTO orcamentoDTO = orcamentoMapper.toDto(orcamento);

        restOrcamentoMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(orcamentoDTO))
            )
            .andExpect(status().isBadRequest());

        List<Orcamento> orcamentoList = orcamentoRepository.findAll();
        assertThat(orcamentoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllOrcamentos() throws Exception {
        // Initialize the database
        orcamentoRepository.saveAndFlush(orcamento);

        // Get all the orcamentoList
        restOrcamentoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(orcamento.getId().intValue())))
            .andExpect(jsonPath("$.[*].ano").value(hasItem(DEFAULT_ANO)))
            .andExpect(jsonPath("$.[*].mes").value(hasItem(DEFAULT_MES)));
    }

    @Test
    @Transactional
    void getOrcamento() throws Exception {
        // Initialize the database
        orcamentoRepository.saveAndFlush(orcamento);

        // Get the orcamento
        restOrcamentoMockMvc
            .perform(get(ENTITY_API_URL_ID, orcamento.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(orcamento.getId().intValue()))
            .andExpect(jsonPath("$.ano").value(DEFAULT_ANO))
            .andExpect(jsonPath("$.mes").value(DEFAULT_MES));
    }

    @Test
    @Transactional
    void getOrcamentosByIdFiltering() throws Exception {
        // Initialize the database
        orcamentoRepository.saveAndFlush(orcamento);

        Long id = orcamento.getId();

        defaultOrcamentoShouldBeFound("id.equals=" + id);
        defaultOrcamentoShouldNotBeFound("id.notEquals=" + id);

        defaultOrcamentoShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultOrcamentoShouldNotBeFound("id.greaterThan=" + id);

        defaultOrcamentoShouldBeFound("id.lessThanOrEqual=" + id);
        defaultOrcamentoShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllOrcamentosByAnoIsEqualToSomething() throws Exception {
        // Initialize the database
        orcamentoRepository.saveAndFlush(orcamento);

        // Get all the orcamentoList where ano equals to DEFAULT_ANO
        defaultOrcamentoShouldBeFound("ano.equals=" + DEFAULT_ANO);

        // Get all the orcamentoList where ano equals to UPDATED_ANO
        defaultOrcamentoShouldNotBeFound("ano.equals=" + UPDATED_ANO);
    }

    @Test
    @Transactional
    void getAllOrcamentosByAnoIsInShouldWork() throws Exception {
        // Initialize the database
        orcamentoRepository.saveAndFlush(orcamento);

        // Get all the orcamentoList where ano in DEFAULT_ANO or UPDATED_ANO
        defaultOrcamentoShouldBeFound("ano.in=" + DEFAULT_ANO + "," + UPDATED_ANO);

        // Get all the orcamentoList where ano equals to UPDATED_ANO
        defaultOrcamentoShouldNotBeFound("ano.in=" + UPDATED_ANO);
    }

    @Test
    @Transactional
    void getAllOrcamentosByAnoIsNullOrNotNull() throws Exception {
        // Initialize the database
        orcamentoRepository.saveAndFlush(orcamento);

        // Get all the orcamentoList where ano is not null
        defaultOrcamentoShouldBeFound("ano.specified=true");

        // Get all the orcamentoList where ano is null
        defaultOrcamentoShouldNotBeFound("ano.specified=false");
    }

    @Test
    @Transactional
    void getAllOrcamentosByAnoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        orcamentoRepository.saveAndFlush(orcamento);

        // Get all the orcamentoList where ano is greater than or equal to DEFAULT_ANO
        defaultOrcamentoShouldBeFound("ano.greaterThanOrEqual=" + DEFAULT_ANO);

        // Get all the orcamentoList where ano is greater than or equal to UPDATED_ANO
        defaultOrcamentoShouldNotBeFound("ano.greaterThanOrEqual=" + UPDATED_ANO);
    }

    @Test
    @Transactional
    void getAllOrcamentosByAnoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        orcamentoRepository.saveAndFlush(orcamento);

        // Get all the orcamentoList where ano is less than or equal to DEFAULT_ANO
        defaultOrcamentoShouldBeFound("ano.lessThanOrEqual=" + DEFAULT_ANO);

        // Get all the orcamentoList where ano is less than or equal to SMALLER_ANO
        defaultOrcamentoShouldNotBeFound("ano.lessThanOrEqual=" + SMALLER_ANO);
    }

    @Test
    @Transactional
    void getAllOrcamentosByAnoIsLessThanSomething() throws Exception {
        // Initialize the database
        orcamentoRepository.saveAndFlush(orcamento);

        // Get all the orcamentoList where ano is less than DEFAULT_ANO
        defaultOrcamentoShouldNotBeFound("ano.lessThan=" + DEFAULT_ANO);

        // Get all the orcamentoList where ano is less than UPDATED_ANO
        defaultOrcamentoShouldBeFound("ano.lessThan=" + UPDATED_ANO);
    }

    @Test
    @Transactional
    void getAllOrcamentosByAnoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        orcamentoRepository.saveAndFlush(orcamento);

        // Get all the orcamentoList where ano is greater than DEFAULT_ANO
        defaultOrcamentoShouldNotBeFound("ano.greaterThan=" + DEFAULT_ANO);

        // Get all the orcamentoList where ano is greater than SMALLER_ANO
        defaultOrcamentoShouldBeFound("ano.greaterThan=" + SMALLER_ANO);
    }

    @Test
    @Transactional
    void getAllOrcamentosByMesIsEqualToSomething() throws Exception {
        // Initialize the database
        orcamentoRepository.saveAndFlush(orcamento);

        // Get all the orcamentoList where mes equals to DEFAULT_MES
        defaultOrcamentoShouldBeFound("mes.equals=" + DEFAULT_MES);

        // Get all the orcamentoList where mes equals to UPDATED_MES
        defaultOrcamentoShouldNotBeFound("mes.equals=" + UPDATED_MES);
    }

    @Test
    @Transactional
    void getAllOrcamentosByMesIsInShouldWork() throws Exception {
        // Initialize the database
        orcamentoRepository.saveAndFlush(orcamento);

        // Get all the orcamentoList where mes in DEFAULT_MES or UPDATED_MES
        defaultOrcamentoShouldBeFound("mes.in=" + DEFAULT_MES + "," + UPDATED_MES);

        // Get all the orcamentoList where mes equals to UPDATED_MES
        defaultOrcamentoShouldNotBeFound("mes.in=" + UPDATED_MES);
    }

    @Test
    @Transactional
    void getAllOrcamentosByMesIsNullOrNotNull() throws Exception {
        // Initialize the database
        orcamentoRepository.saveAndFlush(orcamento);

        // Get all the orcamentoList where mes is not null
        defaultOrcamentoShouldBeFound("mes.specified=true");

        // Get all the orcamentoList where mes is null
        defaultOrcamentoShouldNotBeFound("mes.specified=false");
    }

    @Test
    @Transactional
    void getAllOrcamentosByMesContainsSomething() throws Exception {
        // Initialize the database
        orcamentoRepository.saveAndFlush(orcamento);

        // Get all the orcamentoList where mes contains DEFAULT_MES
        defaultOrcamentoShouldBeFound("mes.contains=" + DEFAULT_MES);

        // Get all the orcamentoList where mes contains UPDATED_MES
        defaultOrcamentoShouldNotBeFound("mes.contains=" + UPDATED_MES);
    }

    @Test
    @Transactional
    void getAllOrcamentosByMesNotContainsSomething() throws Exception {
        // Initialize the database
        orcamentoRepository.saveAndFlush(orcamento);

        // Get all the orcamentoList where mes does not contain DEFAULT_MES
        defaultOrcamentoShouldNotBeFound("mes.doesNotContain=" + DEFAULT_MES);

        // Get all the orcamentoList where mes does not contain UPDATED_MES
        defaultOrcamentoShouldBeFound("mes.doesNotContain=" + UPDATED_MES);
    }

    @Test
    @Transactional
    void getAllOrcamentosByTransacaoIsEqualToSomething() throws Exception {
        Transacao transacao;
        if (TestUtil.findAll(em, Transacao.class).isEmpty()) {
            orcamentoRepository.saveAndFlush(orcamento);
            transacao = TransacaoResourceIT.createEntity(em);
        } else {
            transacao = TestUtil.findAll(em, Transacao.class).get(0);
        }
        em.persist(transacao);
        em.flush();
        orcamento.addTransacao(transacao);
        orcamentoRepository.saveAndFlush(orcamento);
        Long transacaoId = transacao.getId();
        // Get all the orcamentoList where transacao equals to transacaoId
        defaultOrcamentoShouldBeFound("transacaoId.equals=" + transacaoId);

        // Get all the orcamentoList where transacao equals to (transacaoId + 1)
        defaultOrcamentoShouldNotBeFound("transacaoId.equals=" + (transacaoId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultOrcamentoShouldBeFound(String filter) throws Exception {
        restOrcamentoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(orcamento.getId().intValue())))
            .andExpect(jsonPath("$.[*].ano").value(hasItem(DEFAULT_ANO)))
            .andExpect(jsonPath("$.[*].mes").value(hasItem(DEFAULT_MES)));

        // Check, that the count call also returns 1
        restOrcamentoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultOrcamentoShouldNotBeFound(String filter) throws Exception {
        restOrcamentoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restOrcamentoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingOrcamento() throws Exception {
        // Get the orcamento
        restOrcamentoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingOrcamento() throws Exception {
        // Initialize the database
        orcamentoRepository.saveAndFlush(orcamento);

        int databaseSizeBeforeUpdate = orcamentoRepository.findAll().size();

        // Update the orcamento
        Orcamento updatedOrcamento = orcamentoRepository.findById(orcamento.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedOrcamento are not directly saved in db
        em.detach(updatedOrcamento);
        updatedOrcamento.ano(UPDATED_ANO).mes(UPDATED_MES);
        OrcamentoDTO orcamentoDTO = orcamentoMapper.toDto(updatedOrcamento);

        restOrcamentoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, orcamentoDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(orcamentoDTO))
            )
            .andExpect(status().isOk());

        // Validate the Orcamento in the database
        List<Orcamento> orcamentoList = orcamentoRepository.findAll();
        assertThat(orcamentoList).hasSize(databaseSizeBeforeUpdate);
        Orcamento testOrcamento = orcamentoList.get(orcamentoList.size() - 1);
        assertThat(testOrcamento.getAno()).isEqualTo(UPDATED_ANO);
        assertThat(testOrcamento.getMes()).isEqualTo(UPDATED_MES);
    }

    @Test
    @Transactional
    void putNonExistingOrcamento() throws Exception {
        int databaseSizeBeforeUpdate = orcamentoRepository.findAll().size();
        orcamento.setId(longCount.incrementAndGet());

        // Create the Orcamento
        OrcamentoDTO orcamentoDTO = orcamentoMapper.toDto(orcamento);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrcamentoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, orcamentoDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(orcamentoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Orcamento in the database
        List<Orcamento> orcamentoList = orcamentoRepository.findAll();
        assertThat(orcamentoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchOrcamento() throws Exception {
        int databaseSizeBeforeUpdate = orcamentoRepository.findAll().size();
        orcamento.setId(longCount.incrementAndGet());

        // Create the Orcamento
        OrcamentoDTO orcamentoDTO = orcamentoMapper.toDto(orcamento);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrcamentoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(orcamentoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Orcamento in the database
        List<Orcamento> orcamentoList = orcamentoRepository.findAll();
        assertThat(orcamentoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOrcamento() throws Exception {
        int databaseSizeBeforeUpdate = orcamentoRepository.findAll().size();
        orcamento.setId(longCount.incrementAndGet());

        // Create the Orcamento
        OrcamentoDTO orcamentoDTO = orcamentoMapper.toDto(orcamento);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrcamentoMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(orcamentoDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Orcamento in the database
        List<Orcamento> orcamentoList = orcamentoRepository.findAll();
        assertThat(orcamentoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateOrcamentoWithPatch() throws Exception {
        // Initialize the database
        orcamentoRepository.saveAndFlush(orcamento);

        int databaseSizeBeforeUpdate = orcamentoRepository.findAll().size();

        // Update the orcamento using partial update
        Orcamento partialUpdatedOrcamento = new Orcamento();
        partialUpdatedOrcamento.setId(orcamento.getId());

        partialUpdatedOrcamento.ano(UPDATED_ANO);

        restOrcamentoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrcamento.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOrcamento))
            )
            .andExpect(status().isOk());

        // Validate the Orcamento in the database
        List<Orcamento> orcamentoList = orcamentoRepository.findAll();
        assertThat(orcamentoList).hasSize(databaseSizeBeforeUpdate);
        Orcamento testOrcamento = orcamentoList.get(orcamentoList.size() - 1);
        assertThat(testOrcamento.getAno()).isEqualTo(UPDATED_ANO);
        assertThat(testOrcamento.getMes()).isEqualTo(DEFAULT_MES);
    }

    @Test
    @Transactional
    void fullUpdateOrcamentoWithPatch() throws Exception {
        // Initialize the database
        orcamentoRepository.saveAndFlush(orcamento);

        int databaseSizeBeforeUpdate = orcamentoRepository.findAll().size();

        // Update the orcamento using partial update
        Orcamento partialUpdatedOrcamento = new Orcamento();
        partialUpdatedOrcamento.setId(orcamento.getId());

        partialUpdatedOrcamento.ano(UPDATED_ANO).mes(UPDATED_MES);

        restOrcamentoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrcamento.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOrcamento))
            )
            .andExpect(status().isOk());

        // Validate the Orcamento in the database
        List<Orcamento> orcamentoList = orcamentoRepository.findAll();
        assertThat(orcamentoList).hasSize(databaseSizeBeforeUpdate);
        Orcamento testOrcamento = orcamentoList.get(orcamentoList.size() - 1);
        assertThat(testOrcamento.getAno()).isEqualTo(UPDATED_ANO);
        assertThat(testOrcamento.getMes()).isEqualTo(UPDATED_MES);
    }

    @Test
    @Transactional
    void patchNonExistingOrcamento() throws Exception {
        int databaseSizeBeforeUpdate = orcamentoRepository.findAll().size();
        orcamento.setId(longCount.incrementAndGet());

        // Create the Orcamento
        OrcamentoDTO orcamentoDTO = orcamentoMapper.toDto(orcamento);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrcamentoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, orcamentoDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(orcamentoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Orcamento in the database
        List<Orcamento> orcamentoList = orcamentoRepository.findAll();
        assertThat(orcamentoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOrcamento() throws Exception {
        int databaseSizeBeforeUpdate = orcamentoRepository.findAll().size();
        orcamento.setId(longCount.incrementAndGet());

        // Create the Orcamento
        OrcamentoDTO orcamentoDTO = orcamentoMapper.toDto(orcamento);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrcamentoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(orcamentoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Orcamento in the database
        List<Orcamento> orcamentoList = orcamentoRepository.findAll();
        assertThat(orcamentoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOrcamento() throws Exception {
        int databaseSizeBeforeUpdate = orcamentoRepository.findAll().size();
        orcamento.setId(longCount.incrementAndGet());

        // Create the Orcamento
        OrcamentoDTO orcamentoDTO = orcamentoMapper.toDto(orcamento);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrcamentoMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(orcamentoDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Orcamento in the database
        List<Orcamento> orcamentoList = orcamentoRepository.findAll();
        assertThat(orcamentoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteOrcamento() throws Exception {
        // Initialize the database
        orcamentoRepository.saveAndFlush(orcamento);

        int databaseSizeBeforeDelete = orcamentoRepository.findAll().size();

        // Delete the orcamento
        restOrcamentoMockMvc
            .perform(delete(ENTITY_API_URL_ID, orcamento.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Orcamento> orcamentoList = orcamentoRepository.findAll();
        assertThat(orcamentoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
