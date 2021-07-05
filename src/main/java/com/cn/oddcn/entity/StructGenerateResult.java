package com.cn.oddcn.entity;

import java.util.List;

public class StructGenerateResult {
    public String error;

    public List<StructEntity> structEntityList;

    public StructGenerateResult(String error, List<StructEntity> structEntityList) {
        this.error = error;
        this.structEntityList = structEntityList;
    }
}
