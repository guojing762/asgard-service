#!/bin/bash
asgard_service="asgard_service"

asgard_service_table_asgard_orch_saga="asgard_orch_saga"
asgard_service_table_asgard_orch_saga_task="asgard_orch_saga_task"
asgard_service_table_asgard_quartz_method="asgard_quartz_method"

#AsgardDBHOST="192.168.16.214"
BaseDBHOST="localhost"
BaseDBUSER="root"
BaseDBPASS="123456"
BaseDBPORT="3306"

echo "开始同步数据"
# 删除变更服务的saga和sagaTask数据
mysql -u$BaseDBUSER -p$BaseDBPASS -h $BaseDBHOST -P$BaseDBPORT << EOF
use ${asgard_service};
drop procedure if exists sync_data;
delimiter //
create procedure sync_data()
begin

declare sagaNum int;
declare sagaTaskNum int;
declare quartzMethodNum int;

select count(id)  into sagaNum
from ${asgard_service_table_asgard_orch_saga}
where service in ('organization-service','iam-service','state-machine-service' ,'foundation-service','wiki-serivce');
select count(id) into sagaTaskNum
from ${asgard_service_table_asgard_orch_saga_task}
where service in ('organization-service','iam-service','state-machine-service' ,'foundation-service','wiki-serivce');
select count(id) into quartzMethodNum
from ${asgard_service_table_asgard_quartz_method}
where service in ('organization-service','iam-service');
select sagaNum;
select sagaTaskNum;
select quartzMethodNum;

delete
from ${asgard_service_table_asgard_orch_saga}
where service in ('organization-service','iam-service','state-machine-service' ,'foundation-service','wiki-serivce');
delete
from ${asgard_service_table_asgard_orch_saga_task}
where service in ('organization-service','iam-service','state-machine-service' ,'foundation-service','wiki-serivce');
delete
from ${asgard_service_table_asgard_quartz_method}
where service in ('organization-service','iam-service');

select count(id)  into sagaNum
from ${asgard_service_table_asgard_orch_saga}
where service in ('organization-service','iam-service','state-machine-service' ,'foundation-service','wiki-serivce');
select count(id) into sagaTaskNum
from ${asgard_service_table_asgard_orch_saga_task}
where service in ('organization-service','iam-service','state-machine-service' ,'foundation-service','wiki-serivce');
select count(id) into quartzMethodNum
from ${asgard_service_table_asgard_quartz_method}
where service in ('organization-service','iam-service');
select sagaNum;
select sagaTaskNum;
select quartzMethodNum;

end//
delimiter ;

SET @@autocommit=0;
call sync_data;
SET @@autocommit=1;
drop procedure if exists sync_data;
EOF
echo "同步数据结束"