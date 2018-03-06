CREATE DATABASE  IF NOT EXISTS `familyfinance` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `familyfinance`;
-- MySQL dump 10.13  Distrib 5.7.17, for Win64 (x86_64)
--
-- Host: localhost    Database: familyfinance
-- ------------------------------------------------------
-- Server version	5.7.21-log

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `income`
--

DROP TABLE IF EXISTS `income`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `income` (
  `idIncome` int(11) NOT NULL AUTO_INCREMENT,
  `amount` decimal(65,2) NOT NULL,
  `category` int(11) NOT NULL,
  `date` date NOT NULL,
  `userid` int(11) NOT NULL,
  `transactionid` int(11) NOT NULL,
  PRIMARY KEY (`idIncome`),
  KEY `userId_idx` (`userid`),
  KEY `categoryIncome_idx` (`category`),
  KEY `transactionid_idx` (`transactionid`),
  CONSTRAINT `categoryIncome` FOREIGN KEY (`category`) REFERENCES `category` (`idcategory`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `transid` FOREIGN KEY (`transactionid`) REFERENCES `transactiontype` (`idtransactionType`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `userId` FOREIGN KEY (`userid`) REFERENCES `user` (`idUser`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=32 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `income`
--

LOCK TABLES `income` WRITE;
/*!40000 ALTER TABLE `income` DISABLE KEYS */;
INSERT INTO `income` VALUES (8,15.00,2,'2018-02-18',1,2),(9,25.00,2,'2018-02-22',1,2),(10,16.00,2,'2018-02-18',1,2),(11,16.00,2,'2018-02-18',1,2),(12,24.00,2,'2018-02-23',1,2),(14,23.00,2,'2018-02-23',1,2),(15,20.21,2,'2018-02-23',1,2),(16,21.00,2,'2018-02-13',1,2),(17,21.00,2,'2018-02-13',1,2),(28,50.00,5,'2018-02-23',1,2),(29,10.52,2,'2018-02-21',1,2),(31,100.00,18,'2018-02-28',1,2);
/*!40000 ALTER TABLE `income` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2018-03-05 13:34:02
