package projectx.backend.dto;

import lombok.Getter;

@Getter
public class TokenResponse {

    private String access_token;
    private String token_type;
    private int expires_in;
    private String acess_token_token_expired;
}
