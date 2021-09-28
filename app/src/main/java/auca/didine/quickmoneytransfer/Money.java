package auca.didine.quickmoneytransfer;

public class Money {
    private String id,receiver,amount;

    public Money() {
    }

    public Money(String id, String receiver, String amount) {
        this.id = id;
        this.receiver = receiver;
        this.amount = amount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
