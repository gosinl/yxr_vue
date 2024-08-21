package com.yxr.web.controller.core;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.yxr.common.utils.poi.ExcelUtil;
import com.yxr.core.domain.YxrApiShanxipicc;
import com.yxr.core.service.IYxrApiShanxipiccService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.yxr.common.annotation.Log;
import com.yxr.common.core.controller.BaseController;
import com.yxr.common.core.domain.AjaxResult;
import com.yxr.common.enums.BusinessType;
import com.yxr.common.core.page.TableDataInfo;

/**
 * 陕西PICC请求接口Controller
 *
 * @author ruoyi
 * @date 2024-08-21
 */
@RestController
@RequestMapping("/system/shanxipicc")
public class YxrApiShanxipiccController extends BaseController
{
    @Autowired
    private IYxrApiShanxipiccService yxrApiShanxipiccService;

    /**
     * 查询陕西PICC请求接口列表
     */
    @PreAuthorize("@ss.hasPermi('core:shanxipicc:list')")
    @GetMapping("/list")
    public TableDataInfo list(YxrApiShanxipicc yxrApiShanxipicc)
    {
        startPage();
        List<YxrApiShanxipicc> list = yxrApiShanxipiccService.selectYxrApiShanxipiccList(yxrApiShanxipicc);
        return getDataTable(list);
    }

    /**
     * 导出陕西PICC请求接口列表
     */
    @PreAuthorize("@ss.hasPermi('core:shanxipicc:export')")
    @Log(title = "陕西PICC请求接口", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, YxrApiShanxipicc yxrApiShanxipicc)
    {
        List<YxrApiShanxipicc> list = yxrApiShanxipiccService.selectYxrApiShanxipiccList(yxrApiShanxipicc);
        ExcelUtil<YxrApiShanxipicc> util = new ExcelUtil<YxrApiShanxipicc>(YxrApiShanxipicc.class);
        util.exportExcel(response, list, "陕西PICC请求接口数据");
    }

    /**
     * 获取陕西PICC请求接口详细信息
     */
    @PreAuthorize("@ss.hasPermi('core:shanxipicc:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(yxrApiShanxipiccService.selectYxrApiShanxipiccById(id));
    }

    /**
     * 新增陕西PICC请求接口
     */
    @PreAuthorize("@ss.hasPermi('core:shanxipicc:add')")
    @Log(title = "陕西PICC请求接口", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody YxrApiShanxipicc yxrApiShanxipicc)
    {
        return toAjax(yxrApiShanxipiccService.insertYxrApiShanxipicc(yxrApiShanxipicc));
    }

    /**
     * 修改陕西PICC请求接口
     */
    @PreAuthorize("@ss.hasPermi('core:shanxipicc:edit')")
    @Log(title = "陕西PICC请求接口", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody YxrApiShanxipicc yxrApiShanxipicc)
    {
        return toAjax(yxrApiShanxipiccService.updateYxrApiShanxipicc(yxrApiShanxipicc));
    }

    /**
     * 删除陕西PICC请求接口
     */
    @PreAuthorize("@ss.hasPermi('core:shanxipicc:remove')")
    @Log(title = "陕西PICC请求接口", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(yxrApiShanxipiccService.deleteYxrApiShanxipiccByIds(ids));
    }
}
