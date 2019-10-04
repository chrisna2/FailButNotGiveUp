CREATE TABLE `tbl_account` (
  `acct_no` varchar(100) NOT NULL,
  `acct_name` varchar(100) NOT NULL,
  `br_code` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`acct_no`),
  KEY `br_code` (`br_code`),
  CONSTRAINT `tbl_account_ibfk_1` FOREIGN KEY (`br_code`) REFERENCES `tbl_branch` (`br_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `계좌정보` (
  `계좌번호` varchar(100) NOT NULL,
  `계좌명` varchar(100) NOT NULL,
  `관리점코드` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`계좌번호`),
  KEY `관리점코드` (`관리점코드`),
  CONSTRAINT `계좌정보_ibfk_1` FOREIGN KEY (`관리점코드`) REFERENCES `관리점정보` (`관리점코드`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `tbl_branch` (
  `br_code` varchar(10) NOT NULL,
  `br_name` varchar(20) NOT NULL,
  PRIMARY KEY (`br_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `관리점정보` (
  `관리점코드` varchar(10) NOT NULL,
  `관리점명` varchar(20) NOT NULL,
  PRIMARY KEY (`관리점코드`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `tbl_transaction` (
  `tr_date` varchar(100) NOT NULL,
  `acct_no` varchar(100) NOT NULL,
  `tr_no` int(11) NOT NULL DEFAULT '1',
  `tr_amount` bigint(20) DEFAULT '0',
  `tr_fee` bigint(20) DEFAULT '0',
  `can_yn` varchar(10) NOT NULL,
  PRIMARY KEY (`tr_date`,`acct_no`,`tr_no`),
  KEY `acct_no` (`acct_no`),
  CONSTRAINT `tbl_transaction_ibfk_1` FOREIGN KEY (`acct_no`) REFERENCES `tbl_account` (`acct_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `거래내역` (
  `거래일자` varchar(100) NOT NULL,
  `계좌번호` varchar(100) NOT NULL,
  `거래번호` int(11) NOT NULL DEFAULT '1',
  `금액` bigint(20) DEFAULT '0',
  `수수료` bigint(20) DEFAULT '0',
  `취소여부` varchar(10) NOT NULL,
  PRIMARY KEY (`거래일자`,`계좌번호`,`거래번호`),
  KEY `계좌번호` (`계좌번호`),
  CONSTRAINT `거래내역_ibfk_1` FOREIGN KEY (`계좌번호`) REFERENCES `계좌정보` (`계좌번호`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;




