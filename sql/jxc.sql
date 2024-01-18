/*
 Navicat Premium Data Transfer

 Source Server         : 82.156.171.88
 Source Server Type    : MySQL
 Source Server Version : 80029
 Source Host           : 82.156.171.88:6033
 Source Schema         : jxc

 Target Server Type    : MySQL
 Target Server Version : 80029
 File Encoding         : 65001

 Date: 18/01/2024 16:36:26
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_customer
-- ----------------------------
DROP TABLE IF EXISTS `t_customer`;
CREATE TABLE `t_customer`  (
  `customer_id` int NOT NULL AUTO_INCREMENT COMMENT '客户编号id，主键',
  `customer_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '客户名称',
  `contacts` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '联系人',
  `phone_number` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '联系人电话',
  `address` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '客户地址',
  `remarks` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`customer_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '客户' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for t_customer_return_list
-- ----------------------------
DROP TABLE IF EXISTS `t_customer_return_list`;
CREATE TABLE `t_customer_return_list`  (
  `customer_return_list_id` int NOT NULL AUTO_INCREMENT COMMENT '客户退货单id，主键',
  `return_number` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '退货单号',
  `return_date` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '退货日期',
  `amount_paid` float NOT NULL COMMENT '实付金额',
  `amount_payable` float NOT NULL COMMENT '应付金额',
  `state` int NULL DEFAULT NULL COMMENT '状态，是否付款',
  `customer_id` int NULL DEFAULT NULL COMMENT '客户编号id，外键',
  `user_id` int NULL DEFAULT NULL COMMENT '操作员，用户id，外键',
  `remarks` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`customer_return_list_id`) USING BTREE,
  INDEX `customer_id`(`customer_id`) USING BTREE,
  INDEX `user_id`(`user_id`) USING BTREE,
  CONSTRAINT `t_customer_return_list_ibfk_1` FOREIGN KEY (`customer_id`) REFERENCES `t_customer` (`customer_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `t_customer_return_list_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `t_user` (`user_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '退货单' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for t_customer_return_list_goods
-- ----------------------------
DROP TABLE IF EXISTS `t_customer_return_list_goods`;
CREATE TABLE `t_customer_return_list_goods`  (
  `customer_return_list_goods_id` int NOT NULL AUTO_INCREMENT COMMENT '客户退货单商品列表id，主键',
  `goods_id` int NULL DEFAULT NULL COMMENT '商品编号id，外键',
  `goods_code` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商品编码',
  `goods_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商品名称',
  `goods_model` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商品型号',
  `goods_num` int NULL DEFAULT NULL COMMENT '客户退货数量',
  `goods_unit` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商品单位',
  `price` float NOT NULL COMMENT '商品单价',
  `total` float NOT NULL COMMENT '总金额',
  `customer_return_list_id` int NULL DEFAULT NULL COMMENT '客户退货单id，外键',
  `goods_type_id` int NULL DEFAULT NULL COMMENT '商品类别id，外键',
  PRIMARY KEY (`customer_return_list_goods_id`) USING BTREE,
  INDEX `FKtqt67mbn96lxn8hvtl4piblhi`(`customer_return_list_id`) USING BTREE,
  INDEX `FK32ijokbrx3j6h0p6aa9hcccbq`(`goods_type_id`) USING BTREE,
  INDEX `goods_id`(`goods_id`) USING BTREE,
  CONSTRAINT `t_customer_return_list_goods_ibfk_1` FOREIGN KEY (`goods_id`) REFERENCES `t_goods` (`goods_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `t_customer_return_list_goods_ibfk_2` FOREIGN KEY (`goods_type_id`) REFERENCES `t_goods_type` (`goods_type_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `t_customer_return_list_goods_ibfk_3` FOREIGN KEY (`customer_return_list_id`) REFERENCES `t_customer_return_list` (`customer_return_list_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '退货详情' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for t_damage_list
-- ----------------------------
DROP TABLE IF EXISTS `t_damage_list`;
CREATE TABLE `t_damage_list`  (
  `damage_list_id` int NOT NULL AUTO_INCREMENT COMMENT '商品报损单id，主键',
  `damage_number` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商品报损单号',
  `damage_date` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建日期',
  `remarks` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `user_id` int NULL DEFAULT NULL COMMENT '用户id，外键',
  PRIMARY KEY (`damage_list_id`) USING BTREE,
  INDEX `FKpn094ma69sch1icjc2gu7xus`(`user_id`) USING BTREE,
  CONSTRAINT `t_damage_list_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `t_user` (`user_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '报损单' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for t_damage_list_goods
-- ----------------------------
DROP TABLE IF EXISTS `t_damage_list_goods`;
CREATE TABLE `t_damage_list_goods`  (
  `damage_list_goods_id` int NOT NULL AUTO_INCREMENT COMMENT '商品报损单商品列表id，主键',
  `goods_id` int NULL DEFAULT NULL COMMENT '商品编号id，外键',
  `goods_code` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商品编码',
  `goods_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商品名称',
  `goods_model` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商品型号',
  `goods_unit` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商品单位',
  `goods_num` int NULL DEFAULT NULL COMMENT '报损数量',
  `price` float NOT NULL COMMENT '商品单价',
  `total` float NOT NULL COMMENT '总金额',
  `damage_list_id` int NULL DEFAULT NULL COMMENT '商品报损单id，外键',
  `goods_type_id` int NULL DEFAULT NULL COMMENT '商品类别id，外键',
  PRIMARY KEY (`damage_list_goods_id`) USING BTREE,
  INDEX `FKbf5m8mm3gctrnuubr9xkjamj8`(`damage_list_id`) USING BTREE,
  INDEX `FK8r7ietq6opa0ci7uxdqc264yf`(`goods_type_id`) USING BTREE,
  INDEX `goods_id`(`goods_id`) USING BTREE,
  CONSTRAINT `t_damage_list_goods_ibfk_1` FOREIGN KEY (`goods_id`) REFERENCES `t_goods` (`goods_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `t_damage_list_goods_ibfk_2` FOREIGN KEY (`damage_list_id`) REFERENCES `t_damage_list` (`damage_list_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `t_damage_list_goods_ibfk_3` FOREIGN KEY (`goods_type_id`) REFERENCES `t_goods_type` (`goods_type_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '报损单详情' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for t_goods
-- ----------------------------
DROP TABLE IF EXISTS `t_goods`;
CREATE TABLE `t_goods`  (
  `goods_id` int NOT NULL AUTO_INCREMENT COMMENT '商品编号id，主键',
  `goods_code` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商品编码',
  `goods_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商品名称',
  `inventory_quantity` int NOT NULL COMMENT '库存数量',
  `min_num` int NOT NULL COMMENT '库存下限',
  `goods_model` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商品型号',
  `goods_producer` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '生产厂商',
  `purchasing_price` float NOT NULL COMMENT '采购价格',
  `last_purchasing_price` float NOT NULL COMMENT '上一次采购价格',
  `remarks` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `selling_price` float NOT NULL COMMENT '出售价格',
  `state` int NOT NULL COMMENT '0表示初始值,1表示已入库，2表示有进货或销售单据',
  `goods_unit` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '1' COMMENT '商品单位',
  `goods_type_id` int NULL DEFAULT NULL COMMENT '商品类别id，外键',
  `season` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '季节',
  `goods_colour` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '颜色',
  `goods_size` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '尺码',
  `in_purchase` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '内购',
  `retention` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '自留',
  PRIMARY KEY (`goods_id`) USING BTREE,
  INDEX `goods_type_id`(`goods_type_id`) USING BTREE,
  CONSTRAINT `t_goods_ibfk_1` FOREIGN KEY (`goods_type_id`) REFERENCES `t_goods_type` (`goods_type_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 319 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '商品' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for t_goods_type
-- ----------------------------
DROP TABLE IF EXISTS `t_goods_type`;
CREATE TABLE `t_goods_type`  (
  `goods_type_id` int NOT NULL AUTO_INCREMENT COMMENT '商品类别id，主键',
  `goods_type_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商品类别名称',
  `p_id` int NULL DEFAULT NULL COMMENT '父商品类别id',
  `goods_type_state` int NULL DEFAULT NULL COMMENT '类别状态，0为叶子节点',
  PRIMARY KEY (`goods_type_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 24 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '商品类别' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for t_log
-- ----------------------------
DROP TABLE IF EXISTS `t_log`;
CREATE TABLE `t_log`  (
  `log_id` int NOT NULL AUTO_INCREMENT COMMENT '日志id，主键',
  `log_type` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '日志类型',
  `content` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '日志内容',
  `log_date` datetime NULL DEFAULT NULL COMMENT '日志时间',
  `user_id` int NULL DEFAULT NULL COMMENT '用户id，外键',
  PRIMARY KEY (`log_id`) USING BTREE,
  INDEX `FKbvn5yabu3vqwvtjoh32i9r4ip`(`user_id`) USING BTREE,
  CONSTRAINT `t_log_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `t_user` (`user_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 9679 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for t_menu
-- ----------------------------
DROP TABLE IF EXISTS `t_menu`;
CREATE TABLE `t_menu`  (
  `menu_id` int NOT NULL AUTO_INCREMENT COMMENT '菜单id',
  `menu_icon` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '菜单图片',
  `menu_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '菜单名称',
  `p_id` int NULL DEFAULT NULL COMMENT '父菜单id',
  `menu_state` int NULL DEFAULT NULL COMMENT '菜单状态，1表示目录，0表示结点',
  `menu_url` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '菜单的链接地址',
  PRIMARY KEY (`menu_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6051 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for t_overflow_list
-- ----------------------------
DROP TABLE IF EXISTS `t_overflow_list`;
CREATE TABLE `t_overflow_list`  (
  `overflow_list_id` int NOT NULL AUTO_INCREMENT COMMENT '商品报溢单id，主键',
  `overflow_number` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商品报溢单号',
  `overflow_date` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '报溢日期',
  `remarks` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `user_id` int NULL DEFAULT NULL COMMENT '用户id，外键',
  PRIMARY KEY (`overflow_list_id`) USING BTREE,
  INDEX `FK3bu8hj2xniqwbrtg6ls6b8ej2`(`user_id`) USING BTREE,
  CONSTRAINT `t_overflow_list_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `t_user` (`user_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '报溢单' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for t_overflow_list_goods
-- ----------------------------
DROP TABLE IF EXISTS `t_overflow_list_goods`;
CREATE TABLE `t_overflow_list_goods`  (
  `overflow_list_goods_id` int NOT NULL AUTO_INCREMENT COMMENT '商品报溢单商品列表id，主键',
  `goods_id` int NULL DEFAULT NULL COMMENT '商品编号id，外键',
  `goods_code` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商品编码',
  `goods_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商品名称',
  `goods_model` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商品型号',
  `season` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '季节',
  `goods_colour` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '颜色',
  `goods_size` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '尺码',
  `goods_unit` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商品单位',
  `goods_num` int NULL DEFAULT NULL COMMENT '报溢数量',
  `price` float NOT NULL COMMENT '商品单价',
  `total` float NOT NULL COMMENT '总金额',
  `overflow_list_id` int NULL DEFAULT NULL COMMENT '商品报溢单id，外键',
  `goods_type_id` int NULL DEFAULT NULL COMMENT '商品类别id，外键',
  PRIMARY KEY (`overflow_list_goods_id`) USING BTREE,
  INDEX `FKd3s9761mgl456tn2xb0d164h7`(`overflow_list_id`) USING BTREE,
  INDEX `FK20rudkne4kc8uftcenkrng1mn`(`goods_type_id`) USING BTREE,
  INDEX `goods_id`(`goods_id`) USING BTREE,
  CONSTRAINT `t_overflow_list_goods_ibfk_1` FOREIGN KEY (`goods_id`) REFERENCES `t_goods` (`goods_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `t_overflow_list_goods_ibfk_2` FOREIGN KEY (`overflow_list_id`) REFERENCES `t_overflow_list` (`overflow_list_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `t_overflow_list_goods_ibfk_3` FOREIGN KEY (`goods_type_id`) REFERENCES `t_goods_type` (`goods_type_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '报溢单详情' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for t_purchase_list
-- ----------------------------
DROP TABLE IF EXISTS `t_purchase_list`;
CREATE TABLE `t_purchase_list`  (
  `purchase_list_id` int NOT NULL AUTO_INCREMENT COMMENT '进货单id，主键',
  `purchase_number` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '进货单号',
  `amount_paid` float NOT NULL COMMENT '实付金额',
  `amount_payable` float NOT NULL COMMENT '应付金额',
  `purchase_date` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '收货日期',
  `type` int NOT NULL DEFAULT 0 COMMENT '0手动  1导入',
  `remarks` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `state` int NULL DEFAULT NULL COMMENT '状态',
  `supplier_id` int NULL DEFAULT NULL COMMENT '供应商id，外键',
  `user_id` int NULL DEFAULT NULL COMMENT '用户id，外键',
  PRIMARY KEY (`purchase_list_id`) USING BTREE,
  UNIQUE INDEX `purchase_number`(`purchase_number`) USING BTREE,
  INDEX `supplier_id`(`supplier_id`) USING BTREE,
  INDEX `user_id`(`user_id`) USING BTREE,
  CONSTRAINT `t_purchase_list_ibfk_1` FOREIGN KEY (`supplier_id`) REFERENCES `t_supplier` (`supplier_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `t_purchase_list_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `t_user` (`user_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 182 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '进货单' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for t_purchase_list_goods
-- ----------------------------
DROP TABLE IF EXISTS `t_purchase_list_goods`;
CREATE TABLE `t_purchase_list_goods`  (
  `purchase_list_goods_id` int NOT NULL AUTO_INCREMENT COMMENT '进货单商品列表id，主键',
  `goods_id` int NULL DEFAULT NULL COMMENT '商品编号id，外键',
  `goods_code` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商品编码',
  `goods_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商品名称',
  `goods_model` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商品型号',
  `goods_unit` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商品单位',
  `goods_num` int NOT NULL COMMENT '进货数量',
  `price` float NOT NULL COMMENT '商品单价',
  `total` float NOT NULL COMMENT '总金额',
  `purchase_list_id` int NULL DEFAULT NULL COMMENT '进货单id，外键',
  `goods_type_id` int NULL DEFAULT NULL COMMENT '商品类别id，外键',
  PRIMARY KEY (`purchase_list_goods_id`) USING BTREE,
  INDEX `goods_id`(`goods_id`) USING BTREE,
  INDEX `purchase_list_id`(`purchase_list_id`) USING BTREE,
  INDEX `goods_type_id`(`goods_type_id`) USING BTREE,
  CONSTRAINT `t_purchase_list_goods_ibfk_1` FOREIGN KEY (`goods_id`) REFERENCES `t_goods` (`goods_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `t_purchase_list_goods_ibfk_3` FOREIGN KEY (`goods_type_id`) REFERENCES `t_goods_type` (`goods_type_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 1008 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '进货单明细' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for t_return_list
-- ----------------------------
DROP TABLE IF EXISTS `t_return_list`;
CREATE TABLE `t_return_list`  (
  `return_list_id` int NOT NULL AUTO_INCREMENT COMMENT '退货单id，主键',
  `return_number` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '退货单号',
  `return_date` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '退货日期',
  `amount_paid` float NOT NULL COMMENT '实退金额',
  `amount_payable` float NOT NULL COMMENT '应退金额',
  `remarks` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `state` int NULL DEFAULT NULL COMMENT '状态,1表示已退，2表示未退',
  `supplier_id` int NULL DEFAULT NULL COMMENT '供应商id，外键',
  `user_id` int NULL DEFAULT NULL COMMENT '用户id，外键',
  PRIMARY KEY (`return_list_id`) USING BTREE,
  INDEX `FK4qxjj8bvj2etne243xluni0vn`(`supplier_id`) USING BTREE,
  INDEX `FK904juw2v1hm2av0ig26gae9jb`(`user_id`) USING BTREE,
  CONSTRAINT `t_return_list_ibfk_1` FOREIGN KEY (`supplier_id`) REFERENCES `t_supplier` (`supplier_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `t_return_list_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `t_user` (`user_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '退货单' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for t_return_list_goods
-- ----------------------------
DROP TABLE IF EXISTS `t_return_list_goods`;
CREATE TABLE `t_return_list_goods`  (
  `return_list_goods_id` int NOT NULL AUTO_INCREMENT COMMENT '退货单商品列表id，主键',
  `goods_id` int NULL DEFAULT NULL COMMENT '商品编号id，外键',
  `goods_code` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商品编码',
  `goods_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商品名称',
  `goods_model` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商品型号',
  `goods_unit` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商品单位',
  `goods_num` int NULL DEFAULT NULL COMMENT '商品数量',
  `price` float NOT NULL COMMENT '商品单价',
  `total` float NOT NULL COMMENT '总金额',
  `return_list_id` int NULL DEFAULT NULL COMMENT '退货单id，外键',
  `goods_type_id` int NULL DEFAULT NULL COMMENT '商品类别id，外键',
  PRIMARY KEY (`return_list_goods_id`) USING BTREE,
  INDEX `FKemclu281vyvyk063c3foafq1w`(`return_list_id`) USING BTREE,
  INDEX `FKpxnqi9jfkw6wdm1uox2kkr0wk`(`goods_type_id`) USING BTREE,
  INDEX `goods_id`(`goods_id`) USING BTREE,
  CONSTRAINT `t_return_list_goods_ibfk_1` FOREIGN KEY (`return_list_id`) REFERENCES `t_return_list` (`return_list_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `t_return_list_goods_ibfk_3` FOREIGN KEY (`goods_id`) REFERENCES `t_goods` (`goods_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `t_return_list_goods_ibfk_4` FOREIGN KEY (`goods_type_id`) REFERENCES `t_goods_type` (`goods_type_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '退货单详情' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for t_role
-- ----------------------------
DROP TABLE IF EXISTS `t_role`;
CREATE TABLE `t_role`  (
  `role_id` int NOT NULL AUTO_INCREMENT COMMENT '角色id，主键',
  `role_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '角色名称',
  `remarks` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`role_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for t_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `t_role_menu`;
CREATE TABLE `t_role_menu`  (
  `role_menu_id` int NOT NULL AUTO_INCREMENT COMMENT '角色-菜单id',
  `menu_id` int NULL DEFAULT NULL COMMENT '菜单id',
  `role_id` int NULL DEFAULT NULL COMMENT '角色id',
  PRIMARY KEY (`role_menu_id`) USING BTREE,
  INDEX `FKhayg4ib6v7h1wyeyxhq6xlddq`(`menu_id`) USING BTREE,
  INDEX `FKsonb0rbt2u99hbrqqvv3r0wse`(`role_id`) USING BTREE,
  CONSTRAINT `t_role_menu_ibfk_1` FOREIGN KEY (`menu_id`) REFERENCES `t_menu` (`menu_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `t_role_menu_ibfk_2` FOREIGN KEY (`role_id`) REFERENCES `t_role` (`role_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 458 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for t_sale_list
-- ----------------------------
DROP TABLE IF EXISTS `t_sale_list`;
CREATE TABLE `t_sale_list`  (
  `sale_list_id` int NOT NULL AUTO_INCREMENT COMMENT '销售单id，主键',
  `sale_number` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '销售单号',
  `amount_paid` float NOT NULL COMMENT '实付金额',
  `amount_payable` float NOT NULL COMMENT '应付金额',
  `sale_date` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '销售单创建日期',
  `state` int NULL DEFAULT NULL COMMENT '状态',
  `remarks` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `customer_id` int NULL DEFAULT NULL COMMENT '客户id，外键',
  `user_id` int NULL DEFAULT NULL COMMENT '用户id，外键',
  PRIMARY KEY (`sale_list_id`) USING BTREE,
  INDEX `FKox4qfs87eu3fvwdmrvelqhi8e`(`customer_id`) USING BTREE,
  INDEX `FK34bnujemrdqimbhg133enp8k8`(`user_id`) USING BTREE,
  CONSTRAINT `t_sale_list_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `t_user` (`user_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `t_sale_list_ibfk_2` FOREIGN KEY (`customer_id`) REFERENCES `t_customer` (`customer_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for t_sale_list_goods
-- ----------------------------
DROP TABLE IF EXISTS `t_sale_list_goods`;
CREATE TABLE `t_sale_list_goods`  (
  `sale_list_goods_id` int NOT NULL AUTO_INCREMENT COMMENT '销售单商品列表id，主键',
  `goods_id` int NULL DEFAULT NULL COMMENT '商品编号id，外键',
  `goods_code` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商品编码',
  `goods_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商品名称',
  `goods_model` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商品型号',
  `goods_num` int NULL DEFAULT NULL COMMENT '销售数量',
  `goods_unit` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商品单位',
  `price` float NOT NULL COMMENT '商品单价',
  `total` float NOT NULL COMMENT '总金额',
  `sale_list_id` int NULL DEFAULT NULL COMMENT '销售单id，外键',
  `goods_type_id` int NULL DEFAULT NULL COMMENT '商品类别id，外键',
  PRIMARY KEY (`sale_list_goods_id`) USING BTREE,
  INDEX `FK20ehd6ta9geyql4hxtdsnhbox`(`sale_list_id`) USING BTREE,
  INDEX `FK39ej927qf0ldkykafj2nhyu3u`(`goods_type_id`) USING BTREE,
  INDEX `goods_id`(`goods_id`) USING BTREE,
  CONSTRAINT `t_sale_list_goods_ibfk_1` FOREIGN KEY (`goods_id`) REFERENCES `t_goods` (`goods_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `t_sale_list_goods_ibfk_2` FOREIGN KEY (`sale_list_id`) REFERENCES `t_sale_list` (`sale_list_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `t_sale_list_goods_ibfk_3` FOREIGN KEY (`goods_type_id`) REFERENCES `t_goods_type` (`goods_type_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for t_stock_check
-- ----------------------------
DROP TABLE IF EXISTS `t_stock_check`;
CREATE TABLE `t_stock_check`  (
  `id` int NOT NULL,
  `number` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '编号',
  `status` int NULL DEFAULT NULL COMMENT '盘点状态',
  `total_book_quantity` int NULL DEFAULT NULL COMMENT '账面总数量',
  `total_actual_quantity` int NULL DEFAULT NULL COMMENT '实际总数量',
  `total_surplus_quantity` int NULL DEFAULT NULL COMMENT '盘盈总数量',
  `total_surplus_amount` int NULL DEFAULT NULL COMMENT '盘盈总金额',
  `inventory_time` date NULL DEFAULT NULL COMMENT '盘点时间',
  `is_deleted` int NOT NULL DEFAULT 0 COMMENT '0未删1已删',
  `create_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '创建者',
  `update_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '更新者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建日期',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_supplier
-- ----------------------------
DROP TABLE IF EXISTS `t_supplier`;
CREATE TABLE `t_supplier`  (
  `supplier_id` int NOT NULL AUTO_INCREMENT COMMENT '供应商id，主键',
  `supplier_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '供应商名称',
  `contacts` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '联系人',
  `phone_number` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '联系人电话',
  `address` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '供应商地址',
  `remarks` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`supplier_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 21 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '供应商' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for t_take_stock
-- ----------------------------
DROP TABLE IF EXISTS `t_take_stock`;
CREATE TABLE `t_take_stock`  (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `number` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `inventory_quantity` int NULL DEFAULT 0 COMMENT '库存数量',
  `purchase_price` float(10, 2) NULL DEFAULT NULL COMMENT '价格',
  `count_quantity` int NULL DEFAULT 0 COMMENT '盘点数量',
  `surplus_quantity` int NULL DEFAULT NULL COMMENT '盘盈数量',
  `surplus_amount` float NULL DEFAULT NULL COMMENT '盘盈金额',
  `inventory_time` date NULL DEFAULT NULL COMMENT '盘点时间',
  `inventory_variance` int NULL DEFAULT NULL COMMENT '盘点差异(  1盘盈   0 无变化  -1盘亏  )',
  `status` int NULL DEFAULT NULL,
  `is_deleted` int NOT NULL DEFAULT 0 COMMENT '0未删1已删',
  `create_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '创建者',
  `update_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '更新者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建日期',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '盘点' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_take_stock_list
-- ----------------------------
DROP TABLE IF EXISTS `t_take_stock_list`;
CREATE TABLE `t_take_stock_list`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `take_stock_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `goods_id` int NOT NULL COMMENT '商品编号id，主键',
  `goods_code` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商品编码',
  `goods_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商品名称',
  `season` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '季节',
  `goods_colour` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '颜色',
  `goods_size` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '尺码',
  `inventory_quantity` int NULL DEFAULT 0 COMMENT '库存数量',
  `count_quantity` int NULL DEFAULT 0 COMMENT '盘点数量',
  `purchase_price` float(10, 2) NULL DEFAULT NULL COMMENT '采购价格',
  `surplus_quantity` int NULL DEFAULT NULL COMMENT '盘盈数量',
  `surplus_amount` float NULL DEFAULT NULL COMMENT '盘盈金额',
  `inventory_time` date NULL DEFAULT NULL COMMENT '盘点时间',
  `inventory_variance` int NULL DEFAULT NULL COMMENT '盘点差异(  1盘盈   0 无变化  -1盘亏  )',
  `is_deleted` int NOT NULL DEFAULT 0 COMMENT '0未删1已删',
  `remarks` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `create_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '创建者',
  `update_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '更新者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建日期',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 19 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '盘点明细表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for t_unit
-- ----------------------------
DROP TABLE IF EXISTS `t_unit`;
CREATE TABLE `t_unit`  (
  `unit_id` int NOT NULL AUTO_INCREMENT COMMENT '商品单位id，主键',
  `unit_name` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '商品单位名称',
  PRIMARY KEY (`unit_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 16 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for t_user
-- ----------------------------
DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user`  (
  `user_id` int NOT NULL AUTO_INCREMENT COMMENT '用户id，主键',
  `user_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户名',
  `password` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '密码',
  `true_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '真实姓名',
  `roles` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '角色',
  `remarks` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 12 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for t_user_role
-- ----------------------------
DROP TABLE IF EXISTS `t_user_role`;
CREATE TABLE `t_user_role`  (
  `user_role_id` int NOT NULL AUTO_INCREMENT,
  `role_id` int NULL DEFAULT NULL,
  `user_id` int NULL DEFAULT NULL,
  PRIMARY KEY (`user_role_id`) USING BTREE,
  INDEX `role_id`(`role_id`) USING BTREE,
  INDEX `user_id`(`user_id`) USING BTREE,
  CONSTRAINT `t_user_role_ibfk_1` FOREIGN KEY (`role_id`) REFERENCES `t_role` (`role_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `t_user_role_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `t_user` (`user_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 39 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

SET FOREIGN_KEY_CHECKS = 1;
