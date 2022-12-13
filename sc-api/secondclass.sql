/*
 Navicat MySQL Data Transfer

 Source Server         : javadamo
 Source Server Type    : MySQL
 Source Server Version : 80026
 Source Host           : localhost:3306
 Source Schema         : secondclass

 Target Server Type    : MySQL
 Target Server Version : 80026
 File Encoding         : 65001

 Date: 13/12/2022 18:48:06
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_activity
-- ----------------------------
DROP TABLE IF EXISTS `t_activity`;
CREATE TABLE `t_activity`  (
  `aid` bigint NOT NULL AUTO_INCREMENT COMMENT '活动id',
  `aname` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '活动名字',
  `adescription` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '活动描述',
  `a_register_open` datetime NULL DEFAULT NULL COMMENT '报名开始时间',
  `a_register_close` datetime NULL DEFAULT NULL COMMENT '报名结束时间',
  `a_limitted_number` int NULL DEFAULT NULL COMMENT '报名限制人数',
  `a_oid` bigint NULL DEFAULT NULL COMMENT '举办单位',
  `a_uid` bigint NULL DEFAULT NULL COMMENT '活动管理员（即活动申请人）',
  `a_hold_start` datetime NULL DEFAULT NULL COMMENT '举办开始时间',
  `a_hold_end` datetime NULL DEFAULT NULL COMMENT '举办结束时间',
  `astatus` int NULL DEFAULT NULL COMMENT '活动状态\r\n1:活动审核中\r\n2:活动审核通过',
  `apic` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '活动图片路径',
  `a_shichang_num` int NULL DEFAULT NULL COMMENT '活动时长数量',
  `a_shichang_type` bigint NULL DEFAULT NULL COMMENT '活动时长类型',
  `a_address` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '活动地点',
  PRIMARY KEY (`aid`) USING BTREE,
  INDEX `a_oid`(`a_oid`) USING BTREE,
  INDEX `a_shichang_type`(`a_shichang_type`) USING BTREE,
  INDEX `a_uid`(`a_uid`) USING BTREE,
  CONSTRAINT `t_activity_ibfk_1` FOREIGN KEY (`a_oid`) REFERENCES `t_organization` (`oid`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `t_activity_ibfk_2` FOREIGN KEY (`a_uid`) REFERENCES `t_user` (`uid`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_activity
-- ----------------------------
INSERT INTO `t_activity` VALUES (3, '知识分享讲座活动', '邀请校友分享创业经历', '2022-12-01 06:43:11', '2022-12-20 07:43:11', 100, 1, 1, '2022-12-20 02:00:00', '2022-12-20 04:00:00', 2, 'http://dummyimage.com/400x400', 2, 1, '龙洞校区教学楼101');

-- ----------------------------
-- Table structure for t_activity_application
-- ----------------------------
DROP TABLE IF EXISTS `t_activity_application`;
CREATE TABLE `t_activity_application`  (
  `a_app_id` bigint NOT NULL AUTO_INCREMENT COMMENT '申请活动表id（物理id）',
  `uid` bigint NOT NULL COMMENT '申请人id',
  `a_app_type` bigint NULL DEFAULT NULL COMMENT '申请类型',
  `a_app_description` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '申请描述',
  `a_app_attachment` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '申请附件路径',
  `a_app_status` tinyint NULL DEFAULT NULL COMMENT '申请状态 1：申请中 2：通过 0：拒绝',
  `a_app_explain` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '审核说明',
  PRIMARY KEY (`a_app_id`) USING BTREE,
  INDEX `uid`(`uid`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_activity_application
-- ----------------------------
INSERT INTO `t_activity_application` VALUES (4, 1, NULL, '{\"aname\":\"知识分享讲座活动\",\"adescription\":\"邀请校友分享创业经历\",\"aRegisterOpen\":1669876991000,\"aRegisterClose\":1671522191000,\"aLimittedNumber\":100,\"aOid\":1,\"aUid\":1,\"aHoldStart\":1669860000000,\"aHoldEnd\":1671508800000,\"apic\":\"http://dummyimage.com/400x400\",\"aShichangNum\":2,\"aShichangType\":1,\"aAddress\":\"龙洞校区教学楼101\"}', 'http://dummyimage.com/400x400', 2, '通过');

-- ----------------------------
-- Table structure for t_class
-- ----------------------------
DROP TABLE IF EXISTS `t_class`;
CREATE TABLE `t_class`  (
  `cid` bigint NOT NULL AUTO_INCREMENT COMMENT '班级id',
  `cname` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '班级名称',
  `grade` int NULL DEFAULT NULL COMMENT '年级',
  `major` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '专业',
  `college` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '学院',
  `campus` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '校区',
  PRIMARY KEY (`cid`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_class
-- ----------------------------
INSERT INTO `t_class` VALUES (1, '信管1班', 2020, '信息管理与信息系统', '管理学院', '龙洞校区');
INSERT INTO `t_class` VALUES (2, '信管2班', 2020, '信息管理与信息系统', '管理学院', '龙洞校区');
INSERT INTO `t_class` VALUES (3, '信管3班', 2020, '信息管理与信息系统', '管理学院', '龙洞校区');
INSERT INTO `t_class` VALUES (4, '大数据1班', 2020, '大数据管理与应用', '管理学院', '龙洞校区');

-- ----------------------------
-- Table structure for t_organization
-- ----------------------------
DROP TABLE IF EXISTS `t_organization`;
CREATE TABLE `t_organization`  (
  `oid` bigint NOT NULL AUTO_INCREMENT COMMENT '组织id',
  `oname` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '组织名字',
  `uid` bigint NULL DEFAULT NULL COMMENT '负责人id',
  `campus` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '所属校区',
  `odescription` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '组织描述',
  `superior_organization` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '上级单位',
  `permissions_level` int NULL DEFAULT NULL,
  PRIMARY KEY (`oid`) USING BTREE,
  INDEX `uid`(`uid`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_organization
-- ----------------------------
INSERT INTO `t_organization` VALUES (1, '共青团广东工业大学委员会', 1, '龙洞校区', '学校活动管理总部', NULL, 1);
INSERT INTO `t_organization` VALUES (2, '龙洞校区志愿者协会', 1, '龙洞校区', '进行志愿活动', NULL, 2);
INSERT INTO `t_organization` VALUES (3, '广东工业大学乒乓球俱乐部', 2, '大学城校区', '举办乒乓球活动', NULL, 2);
INSERT INTO `t_organization` VALUES (4, '广东工业大学大小招生宣传部（龙洞校区）', 3, '龙洞校区', '举行招生宣传活动', NULL, 2);
INSERT INTO `t_organization` VALUES (5, '摄影社', 1, '龙洞校区', '学习摄影，开展活动', '校团委', 2);

-- ----------------------------
-- Table structure for t_organization_app_shi
-- ----------------------------
DROP TABLE IF EXISTS `t_organization_app_shi`;
CREATE TABLE `t_organization_app_shi`  (
  `shi_app_id` bigint NOT NULL AUTO_INCREMENT,
  `uid` bigint NULL DEFAULT NULL,
  `sid` bigint NULL DEFAULT NULL,
  `shi_app_description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `shi_app_attachment` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `shi_app_status` int NULL DEFAULT NULL,
  PRIMARY KEY (`shi_app_id`) USING BTREE,
  INDEX `uid`(`uid`) USING BTREE,
  INDEX `sid`(`sid`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_organization_app_shi
-- ----------------------------

-- ----------------------------
-- Table structure for t_organization_apply
-- ----------------------------
DROP TABLE IF EXISTS `t_organization_apply`;
CREATE TABLE `t_organization_apply`  (
  `o_app_id` bigint NOT NULL AUTO_INCREMENT,
  `uid` bigint NULL DEFAULT NULL,
  `oid` bigint NULL DEFAULT NULL,
  `o_app_status` int NULL DEFAULT 0 COMMENT '0:申请中；1：申请拒绝；2：申请通过',
  PRIMARY KEY (`o_app_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_organization_apply
-- ----------------------------
INSERT INTO `t_organization_apply` VALUES (1, 3, 5, 2);
INSERT INTO `t_organization_apply` VALUES (5, 2, 3, 2);

-- ----------------------------
-- Table structure for t_organization_member
-- ----------------------------
DROP TABLE IF EXISTS `t_organization_member`;
CREATE TABLE `t_organization_member`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `oid` bigint NULL DEFAULT NULL COMMENT '组织id',
  `uid` bigint NULL DEFAULT NULL COMMENT '用户id',
  `position` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '职位',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `uid`(`uid`) USING BTREE,
  INDEX `oid`(`oid`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_organization_member
-- ----------------------------
INSERT INTO `t_organization_member` VALUES (1, 1, 1, '负责人');
INSERT INTO `t_organization_member` VALUES (2, 2, 1, '负责人');
INSERT INTO `t_organization_member` VALUES (3, 5, 1, '负责人');
INSERT INTO `t_organization_member` VALUES (4, 2, 2, '普通成员');
INSERT INTO `t_organization_member` VALUES (5, 4, 3, '负责人');
INSERT INTO `t_organization_member` VALUES (6, 5, 3, '普通成员');
INSERT INTO `t_organization_member` VALUES (7, 3, 2, '普通成员');

-- ----------------------------
-- Table structure for t_participation
-- ----------------------------
DROP TABLE IF EXISTS `t_participation`;
CREATE TABLE `t_participation`  (
  `pid` bigint NOT NULL COMMENT '参加活动id，可能涉及高并发用自增不合适',
  `uid` bigint NULL DEFAULT NULL COMMENT '用户id',
  `aid` bigint NULL DEFAULT NULL COMMENT '活动id',
  `participate_status` tinyint NULL DEFAULT NULL COMMENT '参与状态 0：等待审核 1：已报名 2：签到 3：签退 4：时长已发放 5:报名失败',
  PRIMARY KEY (`pid`) USING BTREE,
  INDEX `uid`(`uid`) USING BTREE,
  INDEX `aid`(`aid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_participation
-- ----------------------------
INSERT INTO `t_participation` VALUES (126083098596081665, 1, 3, 3);

-- ----------------------------
-- Table structure for t_self_application
-- ----------------------------
DROP TABLE IF EXISTS `t_self_application`;
CREATE TABLE `t_self_application`  (
  `self_app_id` bigint NOT NULL AUTO_INCREMENT COMMENT '自主申报表id',
  `uid` bigint NULL DEFAULT NULL COMMENT '申请人id',
  `self_app_type` bigint NULL DEFAULT NULL COMMENT '申请类型',
  `self_app_shi_num` int NULL DEFAULT NULL COMMENT '申请时长数量',
  `self_app_description` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '申请描述',
  `self_app_attachment` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '申请附件',
  `self_app_statu` tinyint NULL DEFAULT NULL COMMENT '申请状态  1：申请中 2：通过 0：拒绝',
  PRIMARY KEY (`self_app_id`) USING BTREE,
  INDEX `uid`(`uid`) USING BTREE,
  INDEX `self_app_type`(`self_app_type`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_self_application
-- ----------------------------
INSERT INTO `t_self_application` VALUES (1, 1, 1, 2, 'aliquip dolor', 'voluptate non dolore', 2);

-- ----------------------------
-- Table structure for t_semester
-- ----------------------------
DROP TABLE IF EXISTS `t_semester`;
CREATE TABLE `t_semester`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `last_semester_begin` datetime NULL DEFAULT NULL,
  `last_semester_end` datetime NULL DEFAULT NULL,
  `next_semester_begin` datetime NULL DEFAULT NULL,
  `next_semester_end` datetime NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_semester
-- ----------------------------
INSERT INTO `t_semester` VALUES (1, '2022-09-01 00:00:00', '2023-03-01 00:00:00', '2023-03-01 00:00:00', '2023-09-01 00:00:00');

-- ----------------------------
-- Table structure for t_shichang
-- ----------------------------
DROP TABLE IF EXISTS `t_shichang`;
CREATE TABLE `t_shichang`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '时长id',
  `uid` bigint NULL DEFAULT NULL COMMENT '学生id',
  `shi_chang` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '获得学年',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `uid`(`uid`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_shichang
-- ----------------------------
INSERT INTO `t_shichang` VALUES (6, 1, '[[1, [0,\"-\",\"-\"], [0,\"-\",\"-\"], [0,\"-\",\"-\"], [0,\"-\",\"-\"], [4,\"3-3\",\"-\"], [0,\"-\",\"-\"]], [2, [0,\"-\",\"-\"], [0,\"-\",\"-\"], [0,\"-\",\"-\"], [0,\"-\",\"-\"], [0,\"-\",\"-\"], [0,\"-\",\"-\"]], [3, [0,\"-\",\"-\"], [0,\"-\",\"-\"], [0,\"-\",\"-\"], [0,\"-\",\"-\"], [0,\"-\",\"-\"], [0,\"-\",\"-\"]], [4, [0,\"-\",\"-\"], [0,\"-\",\"-\"], [0,\"-\",\"-\"], [0,\"-\",\"-\"], [0,\"-\",\"-\"], [0,\"-\",\"-\"]], [5, [0,\"-\",\"-\"], [0,\"-\",\"-\"], [0,\"-\",\"-\"], [0,\"-\",\"-\"], [0,\"-\",\"-\"], [0,\"-\",\"-\"]]]');
INSERT INTO `t_shichang` VALUES (7, 3120003556, '[[1,[2,\"3\",\"-\"],[0 ,\"-\",\"-\"],[0 ,\"-\",\"-\"],[0 ,\"-\",\"-\"],[0 ,\"-\",\"-\"],[0 ,\"-\",\"-\"]],[2,[0 ,\"-\",\"-\"],[0 ,\"-\",\"-\"],[0 ,\"-\",\"-\"],[0 ,\"-\",\"-\"],[0 ,\"-\",\"-\"],[0 ,\"-\",\"-\"]],[3,[0 ,\"-\",\"-\"],[0 ,\"-\",\"-\"],[0 ,\"-\",\"-\"],[0 ,\"-\",\"-\"],[0 ,\"-\",\"-\"],[0 ,\"-\",\"-\"]],[4,[0 ,\"-\",\"-\"],[0 ,\"-\",\"-\"],[0 ,\"-\",\"-\"],[0 ,\"-\",\"-\"],[0 ,\"-\",\"-\"],[0 ,\"-\",\"-\"]],[5,[0 ,\"-\",\"-\"],[0 ,\"-\",\"-\"],[0 ,\"-\",\"-\"],[0 ,\"-\",\"-\"],[0 ,\"-\",\"-\"],[0 ,\"-\",\"-\"]]]');
INSERT INTO `t_shichang` VALUES (8, 3120003557, '[[1,[0 ,\"-\",\"-\"],[0 ,\"-\",\"-\"],[2,\"3\",\"-\"],[0 ,\"-\",\"-\"],[0 ,\"-\",\"-\"],[0 ,\"-\",\"-\"]],[2,[0 ,\"-\",\"-\"],[0 ,\"-\",\"-\"],[0 ,\"-\",\"-\"],[0 ,\"-\",\"-\"],[0 ,\"-\",\"-\"],[0 ,\"-\",\"-\"]],[3,[0 ,\"-\",\"-\"],[0 ,\"-\",\"-\"],[0 ,\"-\",\"-\"],[0 ,\"-\",\"-\"],[0 ,\"-\",\"-\"],[0 ,\"-\",\"-\"]],[4,[0 ,\"-\",\"-\"],[0 ,\"-\",\"-\"],[0 ,\"-\",\"-\"],[0 ,\"-\",\"-\"],[0 ,\"-\",\"-\"],[0 ,\"-\",\"-\"]],[5,[0 ,\"-\",\"-\"],[0 ,\"-\",\"-\"],[0 ,\"-\",\"-\"],[0 ,\"-\",\"-\"],[0 ,\"-\",\"-\"],[0 ,\"-\",\"-\"]]]');

-- ----------------------------
-- Table structure for t_shichang_application
-- ----------------------------
DROP TABLE IF EXISTS `t_shichang_application`;
CREATE TABLE `t_shichang_application`  (
  `shi_app_id` bigint NOT NULL AUTO_INCREMENT COMMENT '活动时长发放申请表id',
  `uid` bigint NULL DEFAULT NULL COMMENT '申请人id',
  `aid` bigint NULL DEFAULT NULL COMMENT '时长发放的活动id',
  `shi_app_description` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '申请说明',
  `shi_app_status` tinyint NULL DEFAULT NULL COMMENT '申请状态 1：申请中 2：通过 0：拒绝',
  `shi_app_explain` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '审核理由',
  PRIMARY KEY (`shi_app_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_shichang_application
-- ----------------------------
INSERT INTO `t_shichang_application` VALUES (1, 1, 3, '申请发放时长', 1, NULL);

-- ----------------------------
-- Table structure for t_shichang_type
-- ----------------------------
DROP TABLE IF EXISTS `t_shichang_type`;
CREATE TABLE `t_shichang_type`  (
  `sid` bigint NOT NULL AUTO_INCREMENT COMMENT '时长id',
  `shichang_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '时长类型',
  PRIMARY KEY (`sid`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_shichang_type
-- ----------------------------
INSERT INTO `t_shichang_type` VALUES (1, '文体艺术活动');
INSERT INTO `t_shichang_type` VALUES (2, '实践志愿活动');
INSERT INTO `t_shichang_type` VALUES (3, '双创实训活动');
INSERT INTO `t_shichang_type` VALUES (4, '校园建设活动');
INSERT INTO `t_shichang_type` VALUES (5, '理想信念活动');

-- ----------------------------
-- Table structure for t_user
-- ----------------------------
DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user`  (
  `uid` bigint NOT NULL AUTO_INCREMENT COMMENT '用户id',
  `upassword` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户密码',
  `phone` bigint NULL DEFAULT NULL COMMENT '联系方式',
  `cid` bigint NULL DEFAULT NULL COMMENT '班级id',
  `oid` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '所属组织（为空时代表没有加入组织）',
  `uname` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户姓名',
  `grade` int NULL DEFAULT NULL COMMENT '年级',
  PRIMARY KEY (`uid`) USING BTREE,
  INDEX `cid`(`cid`) USING BTREE,
  INDEX `oid`(`oid`) USING BTREE,
  CONSTRAINT `t_user_ibfk_1` FOREIGN KEY (`cid`) REFERENCES `t_class` (`cid`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 3120003559 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_user
-- ----------------------------
INSERT INTO `t_user` VALUES (1, '123456', 123456, 1, '[1,2,5]', '小明', 2020);
INSERT INTO `t_user` VALUES (2, '123456', 12345, 1, '[2,3]', '小红', 2020);
INSERT INTO `t_user` VALUES (3, '123456', 1300, 1, '[3,5]', 'stu_3120003555', 2021);
INSERT INTO `t_user` VALUES (3120003556, '123456', 1300, 1, '[]', 'stu_3120003556', 2022);
INSERT INTO `t_user` VALUES (3120003557, '123456', 1300, 1, '[]', 'stu_3120003557', 2021);
INSERT INTO `t_user` VALUES (3120003558, '123456', 1300, 1, '[]', 'stu_3120003558', 2022);

-- ----------------------------
-- View structure for t_member
-- ----------------------------
DROP VIEW IF EXISTS `t_member`;
CREATE ALGORITHM = UNDEFINED SQL SECURITY DEFINER VIEW `t_member` AS select `t_user`.`uid` AS `uid`,`t_user`.`phone` AS `phone`,`t_user`.`cid` AS `cid`,`t_user`.`uname` AS `uname`,`t_class`.`cname` AS `cname`,`t_class`.`grade` AS `grade`,`t_class`.`major` AS `major`,`t_class`.`college` AS `college`,`t_class`.`campus` AS `campus`,`t_organization_member`.`position` AS `position`,`t_organization_member`.`oid` AS `oid` from ((`t_user` join `t_class` on((`t_user`.`cid` = `t_class`.`cid`))) join `t_organization_member` on((`t_user`.`uid` = `t_organization_member`.`uid`)));

SET FOREIGN_KEY_CHECKS = 1;
