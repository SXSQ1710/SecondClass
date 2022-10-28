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

 Date: 27/10/2022 17:24:34
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_shichang
-- ----------------------------
DROP TABLE IF EXISTS `t_shichang`;
CREATE TABLE `t_shichang`  (
  `sid` bigint NOT NULL AUTO_INCREMENT COMMENT '时长id',
  `shichang_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '时长类型',
  PRIMARY KEY (`sid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_shichang
-- ----------------------------
INSERT INTO `t_shichang` VALUES (1, '文体艺术活动');
INSERT INTO `t_shichang` VALUES (2, '实践志愿活动');
INSERT INTO `t_shichang` VALUES (3, '双创实训活动');
INSERT INTO `t_shichang` VALUES (4, '校园建设活动');
INSERT INTO `t_shichang` VALUES (5, '理想信念活动');

SET FOREIGN_KEY_CHECKS = 1;
