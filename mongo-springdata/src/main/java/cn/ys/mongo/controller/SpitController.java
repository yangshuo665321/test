package cn.ys.mongo.controller;

import cn.ys.entity.PageResult;
import cn.ys.entity.Result;
import cn.ys.entity.StatusCode;
import cn.ys.mongo.pojo.Spit;
import cn.ys.mongo.service.SpitService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/spit")
public class SpitController {

    @Autowired
    private SpitService spitService;

    /**
     * 查询全部数据
     * @return
     */
    @ApiOperation(value = "查询所有吐槽数据")
    @GetMapping
    public Result findAll(){
        return new Result(true, StatusCode.OK, "查询成功", spitService.findAll());
    }

    /**
     * 根据ID查询
     * @param id ID
     * @return
     */
    @ApiOperation(value = "根据 id 查询吐槽数据")
    @ApiImplicitParam(name = "id", value = "用户ID", required = true, dataType = "Integer", paramType = "path")
    @GetMapping("/{id}")
    public Result findOne(@PathVariable String id){
        return new Result(true, StatusCode.OK, "查询成功", spitService.findById(id));
    }

    /**
     * 增加
     * @param spit
     */
    @ApiOperation(value = "增加吐槽数据")
    @ApiImplicitParam(name = "spit", value = "吐槽实体", required = true, dataType = "Spit")
    @PostMapping
    public Result add(@RequestBody Spit spit){
        spitService.add(spit);
        return new Result(true, StatusCode.OK, "增加成功");
    }

    /**
     * 修改
     * @param spit
     */
    @ApiOperation(value = "根据 id 更新吐槽信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "spit", value = "吐槽实体", required = true, dataType = "Spit"),
            @ApiImplicitParam(name = "id", value = "吐槽ID", required = true, dataType = "String", paramType = "path")
    })
    @PutMapping("/{id}")
    public Result update(@RequestBody Spit spit, @PathVariable String id ){
        spit.set_id(id);
        spitService.update(spit);
        return new Result(true,StatusCode.OK,"修改成功");
    }

    /**
     * 删除
     * @param id
     */
    @ApiOperation(value = "根据 id 删除吐槽信息")
    @ApiImplicitParam(name = "id", value = "吐槽ID", required = true, dataType = "String", paramType = "path")
    @DeleteMapping("/{id}")
    public Result deleteById(@PathVariable String id){
        spitService.deleteById(id);
        return new Result(true,StatusCode.OK,"删除成功");
    }

    /**
     * 根据上级ID查询吐槽分页数据
     * @param page
     * @param size
     * @return
     */
    @ApiOperation(value = "根据上级ID查询吐槽分页数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "parentId", value = "父ID", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "page", value = "分页数", required = true, dataType = "Integer", paramType = "path"),
            @ApiImplicitParam(name = "size", value = "每页大小", required = true, dataType = "Integer", paramType = "path")
    })
    @GetMapping("/comment/{parentId}/{page}/{size}")
    public Result findByParentid(@PathVariable String parentId,
                                 @PathVariable int page,
                                 @PathVariable int size){
        Page<Spit> pageList = spitService.findByParentid(parentId, page, size);
        return new Result(true, StatusCode.OK, "查询成功",
                new PageResult<Spit>(pageList.getTotalElements(), pageList.getContent() ) );
    }

}

