package indi.kwanho.powerink.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import indi.kwanho.powerink.entity.mysql.User;
import indi.kwanho.powerink.persistence.mysql.UserMapper;
import indi.kwanho.powerink.service.UserService;
import org.springframework.stereotype.Service;

/**
 * @author kwanho
 * @description 针对表【user】的数据库操作Service实现
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {

}




