drop table if exists server_base;
create table server_base (
    `id` int unsigned primary key auto_increment comment '编号',
    `server_name` varchar(120) not null comment '服务名',
    `server_name_cn` varchar(120) comment '服务中文名',
    `context_path` varchar(60) not null comment '应用path',
    `server_count` int unsigned comment '服务总机器数',
    `server_down` int unsigned comment '机器下线数',
    `server_up` int unsigned comment '机器上线数',
    `display_name` varchar(60) comment '展示名',
    `status` tinyint not null comment '状态 1:使用中 2:过时',
    `create_time` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
    `update_time` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
    unique key uniq_server_name(`server_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='服务基础信息表';

drop table if exists server_info;
create table server_info (
    `id` int unsigned primary key auto_increment comment '编号',
    `server_name` varchar(120) not null comment '服务名',
    `ip_port` varchar(32) not null comment 'ip端口',
    `current_status` varchar(30) not null comment '当前状态 DOWN:下线 UP:上线 HEARTBEAT_LOSE:心跳丢失',
    `heartbeat_distance` int unsigned not null comment '心跳间隔时间(秒)',
    `last_heartbeat_time` bigint unsigned not null comment '上次心跳时间',
    `last_up_time` datetime(3) null comment '最后上线时间',
    `create_time` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
    `update_time` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
    unique key uniq_server_ip(`server_name`, `ip_port`) using btree
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='服务信息表';

drop table if exists server_interface;
create table server_interface (
    `id` int unsigned primary key auto_increment comment '编号',
    `server_name` varchar(120) not null comment '服务名',
    `uri` varchar(240) not null comment '接口uri',
    `name` varchar(100) comment '接口描述',
    `class_name` varchar(200) comment '类名',
    `method_name` varchar(120) comment '方法名',
    `status` tinyint not null comment '状态 1使用中 2过时',
    `rpc_flag` tinyint not null default 1 comment 'rpc标志1是rpc接口 0不是rpc接口',
    `create_time` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
    `update_time` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
    unique key uniq_serv_uri(`server_name`, `uri`) using btree
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='接口信息表';

drop table if exists interface_dependence;
create table interface_dependence (
    `id` int unsigned primary key auto_increment comment '编号',
    `server_name` varchar(120) not null comment '服务名',
    `uri` varchar(240) not null comment '接口uri',
    `dependence_server_name` varchar(120) not null comment '依赖服务名',
    `dependence_server_uri` varchar(240) not null comment '依赖服务uri',
    `status` tinyint not null comment '状态 1使用中 2过时',
    `create_time` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
    `update_time` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
    unique key idx_uri_name_duri_dname(`uri`, `server_name`, `dependence_server_uri`, `dependence_server_name`) using btree,
    index idx_duri(`dependence_server_uri`) using btree
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='接口依赖关系';

drop table if exists call_statistics;
create table call_statistics (
    `id` int unsigned primary key auto_increment comment '编号',
    `server_name` varchar(120) not null comment '服务名',
    `parent_server_name` varchar(120) not null comment '上游服务名',
    `uri` varchar(240) not null comment '接口uri',
    `period` varchar(18) not null comment '统计周期',
    `period_unit` tinyint not null comment '周期单位:1:天 2:小时 3:分钟 4:秒',
    `total_count` int unsigned not null comment '调用总次数',
    `success_count` int unsigned not null comment '成功次数',
    `fail_count` int unsigned not null comment '失败次数',
    `create_time` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
    `update_time` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3) ON UPDATE CURRENT_TIMESTAMP(3),
    unique key uniq_serv_uri_per(`server_name`, `uri`, `parent_server_name`, `period`) using btree,
    index idx_period(`period`) using btree
)  ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='调用统计';

drop table if exists server_change_log;
create table server_change_log (
    `id` int unsigned primary key auto_increment comment '编号',
    `server_name` varchar(120) not null comment '服务名',
    `ip_port` varchar(32) not null comment 'ip端口',
    `before_status` varchar(30) not null comment '变更前状态 DOWN:下线 UP:上线 HEARTBEAT_LOSE:心跳丢失',
    `after_status` varchar(30) not null comment '变更后状态 DOWN:下线 UP:上线 HEARTBEAT_LOSE:心跳丢失',
    `create_time` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='服务状态变化日志表';

drop table if exists trace_log;
create table trace_log (
    `id` bigint unsigned primary key auto_increment comment '编号',
    `trace_id` varchar(40) not null comment '追踪id',
    `span_id` varchar(40) comment '子id',
    `request_url` varchar(500) comment '请求url',
    `request_headers` varchar(300) comment '请求头',
    `request_body` text comment '请求体',
    `response_body` text comment '响应体',
    `self` varchar(800) comment '自身服务信息',
    `remote` varchar(800) comment '远程服务信息',
    `exception` text comment '异常信息',
    `success` tinyint comment '成功标志1成功 0失败',
    `start_time` bigint comment '请求开始时间',
    `during` bigint comment '请求持续时间',
    `create_time` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
    index idx_trace_id(trace_id) using btree,
    index idx_start_time(start_time) using btree
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='调用日志表';
