package com.linkwechat.common.core.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@ApiModel
@Data
public class Tree<T> {

    @ApiModelProperty(name = "id", value = "id")
    private Long id;

    /**
     * 名称
     */
    @ApiModelProperty(name = "label", value = "名称")
    private String name;

    /**
     * 名称
     */
    @ApiModelProperty("可删除标识 0 可删除 1 不可删除")
    private Integer flag;

    /**
     * 父的叶子信息
     */
    @ApiModelProperty(name = "children", value = "父的叶子信息")
    private List<Tree<T>> children;

    /**
     * 父id
     */
    @ApiModelProperty(name = "parentId", value = "父id")
    private Long parentId;

    @ApiModelProperty(name = "hasParent", value = "是否有父节点")
    private boolean hasParent = false;

    @ApiModelProperty(name = "false", value = "是否有叶子节点")
    private boolean hasChildren = false;

    public void initChildren() {
        this.children = new ArrayList<>();
    }

}
