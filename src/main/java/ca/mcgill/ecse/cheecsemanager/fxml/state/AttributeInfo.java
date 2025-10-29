package ca.mcgill.ecse.cheecsemanager.fxml.state;

public class AttributeInfo {
    private String scope;
    private Integer order;

    public AttributeInfo(String scope, Integer order) {
        this.scope = scope;
        this.order = order;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }
}
