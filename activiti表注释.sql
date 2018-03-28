ALTER TABLE `act_evt_log` COMMENT='事件日志';

ALTER TABLE `act_ge_bytearray` COMMENT='二进制数据表';

ALTER TABLE `act_ge_property` COMMENT='属性数据表存储整个流程引擎级别的数据,初始化表结构时，会默认插入三条记录';

ALTER TABLE `act_hi_actinst`  COMMENT='历史节点表';

ALTER TABLE `act_hi_attachment` COMMENT='历史附件表';

ALTER TABLE `act_hi_comment` COMMENT='历史意见表';

ALTER TABLE `act_hi_detail` COMMENT='历史详情表，提供历史变量的查询';

ALTER TABLE `act_hi_identitylink` COMMENT='历史流程人员表';

ALTER TABLE `act_hi_procinst` COMMENT='历史流程实例表';

ALTER TABLE `act_hi_taskinst` COMMENT='历史任务实例表';

ALTER TABLE `act_hi_varinst` COMMENT='历史变量表';

ALTER TABLE `act_id_group` COMMENT='用户组信息表';

ALTER TABLE `act_id_info` COMMENT='用户扩展信息表';

ALTER TABLE `act_id_membership` COMMENT='用户与用户组对应信息表';

ALTER TABLE `act_id_user` COMMENT='用户信息表';

ALTER TABLE `act_procdef_info` COMMENT='流程定义的动态变更信息';

ALTER TABLE `act_re_deployment` COMMENT='部署信息表';

ALTER TABLE `act_re_model` COMMENT='流程设计模型部署表';

ALTER TABLE `act_re_procdef` COMMENT='流程定义数据表';

ALTER TABLE `act_ru_event_subscr` COMMENT='事件监听';

ALTER TABLE `act_ru_execution` COMMENT='运行时流程执行实例表';

ALTER TABLE `act_ru_identitylink` COMMENT='运行时流程人员表，主要存储任务节点与参与者的相关信息';

ALTER TABLE `act_ru_job` COMMENT='异步作业';

ALTER TABLE `act_ru_task` COMMENT='运行时任务节点表';

ALTER TABLE `act_ru_variable` COMMENT='运行时流程变量数据表';