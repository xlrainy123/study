CREATE DATABASE IF NOT EXISTS `online_social_networks` /*!40100 DEFAULT CHARACTER SET latin1 */;
SET GLOBAL local_infile=1;
USE online_social_networks;

CREATE TABLE IF NOT EXISTS relationship (
                      id int PRIMARY KEY not null AUTO_INCREMENT,
                      source_user_id int,
                      target_user_id int,
                      trust_value double,
                      interaction_num int,
                      type varchar(20),
                      UNIQUE KEY (source_user_id, target_user_id, trust_value))
                      DEFAULT CHARSET=utf8;
 CREATE TABLE IF NOT EXISTS user (
                      id int PRIMARY KEY not null AUTO_INCREMENT,
                      education varchar(50),
                      birth varchar(20),
                      nikename varchar(20),
                      zone varchar(20),
                      sex varchar(20),
                      interaction_num int,
                      follower int,
                      create_time datetime,
                      update_time datetime,
                      UNIQUE KEY (birth, sex, nikename))
                      DEFAULT CHARSET=utf8;
/*
insert into user(education,birth,nikename,zone,sex,interaction_num,follower,create_time,update_time)
values
('硕士','1994','user1','北京','男',19,89,'2018-6-16 10:52:12','2018-6-16 10:52:12'),
('本科','1993','user2','上海','男',19,89,'2018-6-16 10:52:12','2018-6-16 10:52:12'),
('博士','1992','user3','天津','男',19,89,'2018-6-16 10:52:12','2018-6-16 10:52:12'),
('硕士','1994','user4','南京','男',19,89,'2018-6-16 10:52:12','2018-6-16 10:52:12'),
('本科','1994','user5','合肥','男',19,89,'2018-6-16 10:52:12','2018-6-16 10:52:12'),
('本科','1994','user6','纽约','男',19,89,'2018-6-16 10:52:12','2018-6-16 10:52:12');
*/
insert into  relationship(source_user_id,target_user_id,trust_value,interaction_num,type)
values
(1,2,0.71,8,'良好'),
(1,3,0.51,10,'及格'),
(1,4,0.91,9,'优秀'),
(1,5,0.41,4,'不及格'),
(1,6,0.99,8,'非常棒'),
(2,6,0.89,10,'良好');
/**
LOAD DATA LOCAL INFILE 'user_node.txt' INTO TABLE user  CHARACTER SET utf8 FIELDS TERMINATED BY '\t' IGNORE 1 LINES;
LOAD DATA LOCAL INFILE 'relationship_node.txt' INTO TABLE relationship CHARACTER SET utf8 FIELDS TERMINATED BY '\t' IGNORE 1 LINES;
