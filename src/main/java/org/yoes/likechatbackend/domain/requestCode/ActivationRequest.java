package org.yoes.likechatbackend.domain.requestCode;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActivationRequest {
    private String email;
    private String code;
}
