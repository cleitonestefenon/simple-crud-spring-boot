package com.github.cleitonestefenon.productms.product.infrastructure;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
class ErrorObject {
    private Integer status_code;
    private String message;
}
