-- MySQL dump 10.13  Distrib 8.0.32, for Win64 (x86_64)
--
-- Host: localhost    Database: cems
-- ------------------------------------------------------
-- Server version	8.0.32

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `performedtest`
--

DROP TABLE IF EXISTS `performedtest`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `performedtest` (
  `id` varchar(45) NOT NULL,
  `subject` varchar(45) NOT NULL,
  `course` varchar(45) NOT NULL,
  `userID` varchar(45) NOT NULL,
  `author` varchar(45) NOT NULL,
  `grade` int NOT NULL,
  `answers` varchar(45) NOT NULL,
  `type` varchar(45) NOT NULL,
  `notes` varchar(300) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `performedtest`
--

LOCK TABLES `performedtest` WRITE;
/*!40000 ALTER TABLE `performedtest` DISABLE KEYS */;
INSERT INTO `performedtest` VALUES ('010101','Math','Algebra','301','yael kanter',30,'00430','Online',NULL),('010101','Math','Algebra','302','yael kanter',10,'12430','Online',NULL),('010101','Math','Algebra','303','yael kanter',50,'00404','Online',NULL),('010101','Math','Algebra','304','yael kanter',30,'31302','Online',NULL),('010101','Math','Algebra','305','yael kanter',90,'00000','Online',NULL),('010101','Math','Algebra','306','yael kanter',40,'44400','Online',NULL),('010101','Math','Algebra','307','yael kanter',30,'14103','Online',NULL),('010101','Math','Algebra','308','yael kanter',40,'10102','Online',NULL),('010107','Math','Algebra','301','yael kanter',80,'000','Online',NULL),('010107','Math','Algebra','302','yael kanter',60,'010','Online',NULL),('010107','Math','Algebra','303','yael kanter',40,'100','Online',NULL),('010107','Math','Algebra','304','yael kanter',80,'000','Online',NULL),('010107','Math','Algebra','305','yael kanter',40,'022','Online',NULL),('010107','Math','Algebra','306','yael kanter',40,'300','Online',NULL),('020205','Programming languages','Java','301','galit aharon',20,'01111','Online',NULL),('020205','Programming languages','Java','302','galit aharon',100,'00000','Online',NULL),('020205','Programming languages','Java','303','galit aharon',40,'03021','Online',NULL),('020205','Programming languages','Java','304','galit aharon',80,'00004','Online',NULL),('020205','Programming languages','Java','305','galit aharon',40,'02120','Online',NULL),('020205','Programming languages','Java','306','galit aharon',60,'30300','Online',NULL),('020205','Programming languages','Java','307','galit aharon',80,'00010','Online',NULL),('010202','Math','Calculus','301','iris kanter',70,'001','Online',NULL),('010202','Math','Calculus','302','iris kanter',100,'000','Online',NULL),('010202','Math','Calculus','303','iris kanter',30,'440','Online',NULL),('010202','Math','Calculus','304','iris kanter',100,'000','Online',NULL),('010202','Math','Calculus','305','iris kanter',40,'303','Online',NULL),('010202','Math','Calculus','306','iris kanter',70,'001','Online',NULL);
/*!40000 ALTER TABLE `performedtest` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-06-20 15:59:05
