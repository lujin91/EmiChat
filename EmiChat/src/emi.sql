CREATE database IF NOT EXISTS `EmiChat`;

use emichat;

DROP TABLE IF EXISTS `tb_user`;
CREATE TABLE `tb_user` (
  `uid` char(32) NOT NULL ,
  `uname` varchar(50) NOT NULL unique,
  `password` varchar(50) NOT NULL,
  `headImg` varchar(100) NOT NULL,  
  `emailAddr` varchar(100) NOT NULL,
  `nickname` varchar(30) NOT NULL,
  `birthday` DATE NOT NULL,
  `status` int(1) NOT NULL,
  PRIMARY KEY (`uid`)
);

DROP TABLE IF EXISTS `tb_relation`;
CREATE TABLE `tb_relation` (
  `rid` char(32) NOT NULL ,
  `remarkName` varchar(30) NOT NULL,
  `cid` char(32) NOT NULL,
  `uid` char(32) NOT NULL,
  PRIMARY KEY (`rid`)
);
ALTER TABLE tb_relation ADD UNIQUE KEY(cid,uid);
ALTER TABLE tb_relation ADD CONSTRAINT FK_RELATION_USER_1 FOREIGN KEY (cid) REFERENCES tb_user (uid) on delete cascade;
ALTER TABLE tb_relation ADD CONSTRAINT FK_RELATION_USER_2 FOREIGN KEY (uid) REFERENCES tb_user (uid) on delete cascade;

DROP TABLE IF EXISTS `tb_group`;
CREATE TABLE `tb_group` (
  `gid` char(32) NOT NULL ,
  `groupName` varchar(50) NOT NULL unique,
  `createTime` DATETIME NOT NULL,
  `createrId` char(32) NOT NULL,
  PRIMARY KEY (`gid`)
);

DROP TABLE IF EXISTS `tb_member`;
CREATE TABLE `tb_member` (
  `mid` char(32) NOT NULL,
  `uid` char(32) NOT NULL,
  `gid` char(32) NOT NULL,
  `alias` varchar(30) NOT NULL,
  `role` int(1) NOT NULL,
  PRIMARY KEY (`mid`)
);
ALTER TABLE tb_member ADD UNIQUE KEY(uid,gid);
ALTER TABLE tb_member ADD CONSTRAINT FK_MEMBER_USER FOREIGN KEY (uid) REFERENCES tb_user (uid) on delete cascade;
ALTER TABLE tb_member ADD CONSTRAINT FK_MEMBER_GROUP FOREIGN KEY (gid) REFERENCES tb_group (gid) on delete cascade;

DROP TABLE IF EXISTS `tb_message`;
CREATE TABLE `tb_message` (
  `id` int primary key auto_increment,
  `sendId` char(32) NOT NULL,
  `recvGrpId` char(32),
  `recvId` char(32),
  `sendTime` DATETIME NOT NULL,
  `type` int(1) NOT NULL,
  `content` varchar(500) NOT NULL
);
ALTER TABLE tb_message ADD CONSTRAINT FK_MESSAGE_USER_1 FOREIGN KEY (sendId) REFERENCES tb_user (uid) on delete cascade;
ALTER TABLE tb_message ADD CONSTRAINT FK_MESSAGE_USER_2 FOREIGN KEY (recvId) REFERENCES tb_user (uid) on delete cascade;
ALTER TABLE tb_message ADD CONSTRAINT FK_MESSAGE_GROUP FOREIGN KEY (recvGrpId) REFERENCES tb_group (gid) on delete cascade;


CREATE TABLE `tb_user` (
  `uid` bigint NOT NULL auto_increment,
  `user_name` varchar(50) NOT NULL unique,
  `password` varchar(50) NOT NULL,
  `headImg` varchar(100) NOT NULL,  
  `emailAddr` varchar(100) NOT NULL,
  `nickname` varchar(30) NOT NULL,
  `birthday` DATE NOT NULL,
  `status` int(1) NOT NULL,
  PRIMARY KEY (`uid`)
);

