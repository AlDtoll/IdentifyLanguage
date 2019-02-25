package com.example.identifylanguage.common;

/**
 * Списко констант, которые используются в приложении
 */
public enum ConstantEnum {
    PRESENTER("presenter"),
    BASE_URL("https://gateway.watsonplatform.net/language-translator/api/"),
    USERNAME("6987a48d-342e-4a69-8adc-e65b1cc0b9da"),
    PASSWORD("MxYSIi6nQP2Y");
    private String code;

    ConstantEnum(String code) {
        this.code = code;
    }

    /**
     * Получение enum по соответствующему коду
     *
     * @param code код
     * @return enum
     */
    public static ConstantEnum of(String code) {
        for (ConstantEnum constant : values()) {
            if (constant.code.equalsIgnoreCase(code)) {
                return constant;
            }
        }
        throw new IllegalArgumentException("такого эффекта нет");
    }

    public String getCode() {
        return code;
    }
}
