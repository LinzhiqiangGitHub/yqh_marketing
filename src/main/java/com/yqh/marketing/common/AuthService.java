
package com.yqh.marketing.common;

import java.util.List;


public interface AuthService {

    /**
     * 添加第三方登录
     * 
     * @param auth
     * @return int
     */
    int insertAuth(Auth auth);

    /**
     * 删除第三方登录
     * 
     * @param authIds
     *            删除人
     * @return int
     */
    int deleteAuth(String[] authIds);

    /**
     * 根据id查询信息
     * 
     * @param authId
     * @return Auth
     */
    Auth findAuthByAuthId(Long authId);

    /**
     * 修改第三方登录信息
     * 
     * @param auth
     * @return int
     */
    int updateAuth(Auth auth);

    /**
     * 根据类型查询
     */
    Auth findAuthByAuthType(String authType);

    /**
     * 根据第三方登录接口ID修改启用状态
     * 
     * @param authId
     * @return
     */
    boolean updateUserdStatus(Long authId);

    /**
     * 查找已启用的第三方登录接口
     * 
     * @return
     */
    List<Auth> findByShow();

    /**
     * 根据主键删除记录
     * @param authId 主键id
     * @return 执行结果
     */
    int deleteAuthById(Long authId);

}
