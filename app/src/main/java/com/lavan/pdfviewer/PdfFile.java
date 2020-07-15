package com.lavan.pdfviewer;

public class PdfFile {
    String name, path, size;
    long sizeL;

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public PdfFile(String name, String path, String size, long SizeL) {
        this.name = name;
        this.path = path;
        this.size = size;
        this.sizeL = SizeL;
    }

    public long getSizeL() {
        return sizeL;
    }

    public void setSizeL(long sizeL) {
        this.sizeL = sizeL;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
