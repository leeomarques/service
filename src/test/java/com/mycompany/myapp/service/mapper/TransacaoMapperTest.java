package com.mycompany.myapp.service.mapper;

import org.junit.jupiter.api.BeforeEach;

class TransacaoMapperTest {

    private TransacaoMapper transacaoMapper;

    @BeforeEach
    public void setUp() {
        transacaoMapper = new TransacaoMapperImpl();
    }
}
