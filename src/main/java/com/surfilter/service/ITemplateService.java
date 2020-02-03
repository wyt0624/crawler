package com.surfilter.service;

import com.surfilter.entity.Columns;
import com.surfilter.entity.Template;

import java.util.List;
import java.util.Map;

public interface ITemplateService {
    /**
     * 钓鱼或仿冒网站模板
     * @param columns
     * @return
     */
    List<Template> listTemplate(Columns columns);

    /***
     * 新建或更新网站模板
     * @param template
     */
    Map<String, Object> optTemplate(Template template) throws Exception;

    /**
     * 检查url是否已经存在。
     * @param template
     * @return
     */
    Map<String, Object> checkUrl(Template template);

    /**
     * 删处网站模板。
     * @param template
     * @return
     */
    Map<String, Object> delTemplate(Template template);
}
