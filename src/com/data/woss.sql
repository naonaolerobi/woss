--创建用户并授权

create user woss identified by woss;
grant resource,connect to woss;

--woss用户登录

delete from T_DETAIL_16;

--删除原有表或索引
begin 
	for i in 1..31 loop
		execute immediate 
			'drop table T_detail_'||to_char(i);
		execute immediate 
			'drop index i_detail_'||to_char(i);
	end loop;
end;

--创建表和索引
begin 
	for i in 1..31 loop
		execute immediate 
			'create table T_detail_'||to_char(i)||
			'(
				aaa_login_name varchar2(10),
				login_ip varchar2(32),
				login_date timestamp,
				logout_date timestamp,
				nas_ip varchar2(32),
				time_duration number(10) 
			)';
			execute immediate 
				'create index i_detail_'||to_char(i)||'
							 on t_detail_'||to_char(i)||' (aaa_login_name)';
end loop;
end ;