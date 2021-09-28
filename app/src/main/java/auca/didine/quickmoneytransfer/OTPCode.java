package auca.didine.quickmoneytransfer;

public class OTPCode {
    private String client,code;

    public OTPCode() {
    }

    public OTPCode(String client, String code) {
        this.client = client;
        this.code = code;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
