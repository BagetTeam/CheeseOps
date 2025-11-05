package ca.mcgill.ecse.cheecsemanager.fxml.state;

public class TOForm <K, T> {
    private K key;
    private T data;
    private final PageType pageType;

    public TOForm(K key, T data, PageType pageType) {
        this.key = key;
        this.data = data;
        this.pageType = pageType;
    }

    public TOForm(PageType pageType) {
        this.pageType = pageType;
    }

    public TOForm(T data, PageType pageType) {
        this.data = data;
        this.pageType = pageType;
    }

    public K getKey() {
        return key;
    }

    public T getData() {
        return data;
    }

    public void setKey(K key) {
        this.key = key;
    }

    public void setData(T data) {
        this.data = data;
    }

    public PageType getPageType() {
        return pageType;
    }
}
