package com.dv.oficina.dto.request;

import com.dv.oficina.model.TipoItem;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class ItemRequestTest {

    private final Validator validator =
            Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    void aceitaValorComDuasCasasDecimais() {
        ItemRequest item = new ItemRequest(
                TipoItem.PECA, "Pastilha", 3, new BigDecimal("33.33"));

        assertThat(validator.validate(item)).isEmpty();
    }

    @Test
    void rejeitaValorComMaisDeDuasCasasDecimais() {
        ItemRequest item = new ItemRequest(
                TipoItem.PECA, "Pastilha", 3, new BigDecimal("33.333"));

        assertThat(validator.validate(item)).isNotEmpty();
    }
}