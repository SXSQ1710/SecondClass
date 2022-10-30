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

 Date: 30/10/2022 15:14:34
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_acticity_application
-- ----------------------------
DROP TABLE IF EXISTS `t_acticity_application`;
CREATE TABLE `t_acticity_application`  (
  `a_app_id` bigint NOT NULL AUTO_INCREMENT COMMENT '申请活动表id（物理id）',
  `aid` bigint NOT NULL COMMENT '申请活动id',
  `uid` bigint NOT NULL COMMENT '申请人id',
  `a_app_type` bigint NULL DEFAULT NULL COMMENT '申请类型',
  `a_app_description` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '申请描述',
  `a_app_attachment` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '申请附件路径',
  `a_app_status` tinyint NULL DEFAULT NULL COMMENT '申请状态 1：申请中 2：通过 0：拒绝',
  PRIMARY KEY (`a_app_id`) USING BTREE,
  INDEX `uid`(`uid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_acticity_application
-- ----------------------------

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
  `astatus` int NULL DEFAULT NULL COMMENT '活动状态',
  `apic` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '活动图片路径',
  `a_shichang_num` int NULL DEFAULT NULL COMMENT '活动时长数量',
  `a_shichang_type` bigint NULL DEFAULT NULL COMMENT '活动时长类型',
  `a_address` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '活动地点',
  PRIMARY KEY (`aid`) USING BTREE,
  INDEX `a_oid`(`a_oid`) USING BTREE,
  INDEX `a_shichang_type`(`a_shichang_type`) USING BTREE,
  INDEX `a_uid`(`a_uid`) USING BTREE,
  CONSTRAINT `t_activity_ibfk_1` FOREIGN KEY (`a_oid`) REFERENCES `t_oganization` (`oid`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `t_activity_ibfk_2` FOREIGN KEY (`a_uid`) REFERENCES `t_user` (`uid`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_activity
-- ----------------------------

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
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_class
-- ----------------------------
INSERT INTO `t_class` VALUES (1, '信管1班', 2020, '信息管理与信息系统', '管理学院', '龙洞校区');

-- ----------------------------
-- Table structure for t_oganization
-- ----------------------------
DROP TABLE IF EXISTS `t_oganization`;
CREATE TABLE `t_oganization`  (
  `oid` bigint NOT NULL AUTO_INCREMENT COMMENT '组织id',
  `oname` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '组织名字',
  `uid` bigint NULL DEFAULT NULL COMMENT '负责人id',
  `campus` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '所属校区',
  `odescription` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '组织描述',
  `superior_oganization` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '上级单位',
  PRIMARY KEY (`oid`) USING BTREE,
  INDEX `uid`(`uid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_oganization
-- ----------------------------
INSERT INTO `t_oganization` VALUES (1, '校团委', 1, '龙洞校区', '学校老大', NULL);

-- ----------------------------
-- Table structure for t_oganization_app_shi
-- ----------------------------
DROP TABLE IF EXISTS `t_oganization_app_shi`;
CREATE TABLE `t_oganization_app_shi`  (
  `shi_app_id` bigint NOT NULL AUTO_INCREMENT,
  `uid` bigint NULL DEFAULT NULL,
  `sid` bigint NULL DEFAULT NULL,
  `shi_app_description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `shi_app_attachment` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `shi_app_status` int NULL DEFAULT NULL,
  PRIMARY KEY (`shi_app_id`) USING BTREE,
  INDEX `uid`(`uid`) USING BTREE,
  INDEX `sid`(`sid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_oganization_app_shi
-- ----------------------------

-- ----------------------------
-- Table structure for t_oganization_member
-- ----------------------------
DROP TABLE IF EXISTS `t_oganization_member`;
CREATE TABLE `t_oganization_member`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `oid` bigint NULL DEFAULT NULL COMMENT '组织id',
  `uid` bigint NULL DEFAULT NULL COMMENT '用户id',
  `position` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '职位',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `uid`(`uid`) USING BTREE,
  INDEX `oid`(`oid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_oganization_member
-- ----------------------------

-- ----------------------------
-- Table structure for t_paticipation
-- ----------------------------
DROP TABLE IF EXISTS `t_paticipation`;
CREATE TABLE `t_paticipation`  (
  `pid` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '参加活动id，可能涉及高并发用自增不合适',
  `uid` bigint NULL DEFAULT NULL COMMENT '用户id',
  `aid` bigint NULL DEFAULT NULL COMMENT '活动id',
  `participate_status` tinyint NULL DEFAULT NULL COMMENT '参与状态 1：已报名 2：签到 3：签退 4：时长已发放',
  PRIMARY KEY (`pid`) USING BTREE,
  INDEX `uid`(`uid`) USING BTREE,
  INDEX `aid`(`aid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_paticipation
-- ----------------------------

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
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_self_application
-- ----------------------------

-- ----------------------------
-- Table structure for t_shichang
-- ----------------------------
DROP TABLE IF EXISTS `t_shichang`;
CREATE TABLE `t_shichang`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '时长id',
  `uid` bigint NULL DEFAULT NULL COMMENT '学生id',
  `sid` bigint NULL DEFAULT NULL COMMENT '时长类型id',
  `snum` int NULL DEFAULT NULL COMMENT '时长数量',
  `acquire_time` date NULL DEFAULT NULL COMMENT '获得学年',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `uid`(`uid`) USING BTREE,
  INDEX `sid`(`sid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_shichang
-- ----------------------------

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
  `oid` bigint NULL DEFAULT NULL COMMENT '所属组织（为空时代表没有加入组织）',
  PRIMARY KEY (`uid`) USING BTREE,
  INDEX `cid`(`cid`) USING BTREE,
  INDEX `oid`(`oid`) USING BTREE,
  CONSTRAINT `t_user_ibfk_1` FOREIGN KEY (`cid`) REFERENCES `t_class` (`cid`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `t_user_ibfk_2` FOREIGN KEY (`oid`) REFERENCES `t_oganization` (`oid`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_user
-- ----------------------------
INSERT INTO `t_user` VALUES (1, '123456', 123456, 1, 1);

SET FOREIGN_KEY_CHECKS = 1;
