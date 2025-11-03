package ca.mcgill.ecse.cheecsemanager.fxml.state;

import java.util.Map;

public class NavigationState <T> {
    private String title;
    private PageType pageType;
    private String multiplicity;
    private String pageName;
    private Map<String, AttributeInfo> scope;
    private T data;

    public NavigationState(String title, PageType pageType, String pageName) {
        this.title = title;
        this.pageType = pageType;
        this.pageName = pageName;
    }

    public PageType getPageType() {
        return pageType;
    }

    public String getMultiplicity() {
        return multiplicity;
    }

    public String getTitle() {
        return title;
    }

    public T getData() {
        return data;
    }

    public String getPageName() {
        return pageName;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setData(T data) {
        this.data = data;
    }

    public void setPageName(String pageName) {
        this.pageName = pageName;
    }

    public void setMultiplicity(String multiplicity) {
        this.multiplicity = multiplicity;
    }

    public void setPageType(PageType pageType) {
        this.pageType = pageType;
    }

    public Map<String, AttributeInfo> getScope() {
        return scope;
    }

    public void setScope(Map<String, AttributeInfo> scope) {
        this.scope = scope;
    }
}
