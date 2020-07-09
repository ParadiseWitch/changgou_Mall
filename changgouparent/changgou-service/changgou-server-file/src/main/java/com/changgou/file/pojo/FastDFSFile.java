package com.changgou.file.pojo;

import java.io.Serializable;

/**
 * @ClassName com.changgou.file.pojo.FastDFSFile
 * @Description 文件信息封装类
 * @Author Maid
 * @Date 2020/7/9 0009 15:35
 * @Version v1.0
 */
public class FastDFSFile implements Serializable {
    //文件名
    private String name;
    //文件内容
    private byte[] content;
    //扩展名
    private String ext;
    //MD5摘要值
    private String md5;
    //创建作者
    private String author;

    public FastDFSFile(String name, byte[] content, String ext, String author) {
        this.name = name;
        this.content = content;
        this.ext = ext;
        this.author = author;
    }

    public FastDFSFile(String name, byte[] content, String ext) {
        this.name = name;
        this.content = content;
        this.ext = ext;
    }

    //-------getter&setter---

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public String getExt() {
        return ext;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
