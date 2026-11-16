package com.dv.oficina.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class PdfFormatos {

    private static final DecimalFormatSymbols SIMBOLOS =
            DecimalFormatSymbols.getInstance(Locale.of("pt", "BR"));

    /** 1234.5 -> "1.234,50" */
    public String valor(BigDecimal numero) {
        if (numero == null) {
            return "";
        }
        return new DecimalFormat("#,##0.00", SIMBOLOS).format(numero);
    }

    /** 1234.5 -> "R$ 1.234,50" */
    public String moeda(BigDecimal numero) {
        return numero == null ? "" : "R$ " + valor(numero);
    }

    /** "11222333000181" -> "11.222.333/0001-81" */
    public String cnpj(String digitos) {
        if (digitos == null || digitos.length() != 14) {
            return digitos;
        }
        return digitos.substring(0, 2) + "." + digitos.substring(2, 5) + "."
                + digitos.substring(5, 8) + "/" + digitos.substring(8, 12) + "-"
                + digitos.substring(12);
    }
}