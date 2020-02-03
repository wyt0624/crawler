package com.surfilter.dao;

import com.surfilter.entity.Template;

import java.util.List;
import java.util.Map;

public interface TemplateMapper {
    /**
     * 钓鱼网站模板。
     * @param param
     * @return
     */
    List<Template> listTemplate(Map<String, Object> param);

    /**
     * 修改模板数据
     * @param template
     */
    void changeTemplate(Template template);

    /**
     * 新建模板数据
     * @param template
     */
    void addTemplate(Template template);

    /**
     * 检查url是否已经在模板中了
     * @param template
     */
    int checkUrl(Template template);

    /**
     * 删处模板
     * @param template
     */
    void delTemplate(Template template);
}
