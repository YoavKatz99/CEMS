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
-- Table structure for table `question`
--

DROP TABLE IF EXISTS `question`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `question` (
  `id` varchar(5) NOT NULL,
  `subject` text NOT NULL,
  `courseName` text NOT NULL,
  `questionText` text NOT NULL,
  `questionNumber` int NOT NULL,
  `lecturer` text NOT NULL,
  `op1` text NOT NULL,
  `op2` text NOT NULL,
  `op3` text NOT NULL,
  `op4` text NOT NULL,
  `correctop` text NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `question`
--

LOCK TABLES `question` WRITE;
/*!40000 ALTER TABLE `question` DISABLE KEYS */;
INSERT INTO `question` VALUES ('01124','Math','Algebra','12+34',124,'yael kanter','24','46','48','1234','46'),('01126','Math','Algebra','3*4	',126,'yael kanter','4','8','12','16','12'),('01127','Math','Algebra','9-9+2',127,'yael kanter','0','2','1','-2','2'),('01128','Math','Algebra','X=3,Y=2\nX+Y=?',128,'yael kanter','5','1','6','4','5'),('01129','Math','Algebra','6-3-6=?',129,'yael kanter','-3','-6','-9','0','-3'),('01131','Math','Calculus','1+4	= ?',131,'iris kanter','4','5','6','8','5'),('01132','Math','Calculus','2*7 = ?',132,'iris kanter','14','7','-5','1','14'),('01133','Math','Calculus','4-3 = ?',133,'iris kanter','7','-1','12','1','1'),('01137','Math','Calculus','What is the derivative of sin(x)?',137,'iris kanter','cos(x)',' tan(x)','sec(x)','cot(x)','cos(x)'),('01138','Math','Calculus','What is the integral of 2x?',138,'iris kanter',' x^3','2x^2',' x^2',' 1/x',' x^2'),('01139','Math','Calculus','What is the limit of (1 + 1/x)^x as x approaches infinity?',139,'iris kanter','0','1','e','infinity','e'),('02135','Programming languages','Java','int is?',135,'galit aharon','1','a','-2.4','1.1','1'),('02149','Programming languages','Java','What is a Java keyword for defining a class?',149,'galit aharon','define','class','struct','public','public'),('02150','Programming languages','Java','What is the symbol used for the assignment operator in Java?',150,'galit aharon','=','==',':=','+=','+='),('02151','Programming languages','Java','What is the Java keyword for creating an instance of a class?',151,'galit aharon','instance','object','new ','create','new '),('02152','Programming languages','Java','Which Java data type is used to store whole numbers?',152,'galit aharon','float','int','double','boolean','int'),('02153','Programming languages','C','Which C data type is used to store single characters?',153,'riki green','char','string','int','boolean','char'),('02154','Programming languages','C','What is the C symbol used to end a statement?',154,'riki green',';','/','.','!',';'),('02155','Programming languages','C','What is the C symbol used for the addition operator?',155,'riki green','+','-','/','*','+'),('02156','Programming languages','C','What is the C keyword used to define a function?',156,'riki green','def','func','define','void','void'),('02157','Programming languages','C','What is the C symbol used for the equality comparison?',157,'riki green','==','!=','=','//','=='),('03130','Physics','Mechanics','m=3[kg], n=500[g]\nm-n=?',130,'alon aloni','2[kg]','2.5[kg]','2.5[g]','3.5[kg]','2.5[kg]'),('03140','Physics','Mechanics','What is the formula for kinetic energy?',140,'alon aloni','F = ma','E = mc^2','E = 1/2 mv^2 ','P = mg','E = 1/2 mv^2 '),('03141','Physics','Mechanics','What is the unit of force?',141,'alon aloni','kg','N','m/s','J','N'),('03142','Physics','Mechanics','What is the acceleration due to gravity on Earth?',142,'alon aloni','9.8 m/s^2','3.0 m/s^2','6.7 m/s^2','1.0 m/s^2','9.8 m/s^2'),('03143','Physics','Mechanics','What is the formula for calculating work done?',143,'alon aloni','W = mg','W = Fd ','W = mv','W = 1/2 mv^2','W = Fd '),('03144','Physics','Electricity','What is the SI unit of electric current?',144,'ilana hadar','Ampere','Volt','Ohm','Watt','Ampere'),('03145','Physics','Electricity','What is the formula for calculating electric power?',145,'ilana hadar','P = IV','P = IR','P = V/R','P = I/R',' P = IV'),('03146','Physics','Electricity','What does AC stand for in AC current?',146,'ilana hadar','Alternating Current','Amperes and Coulombs','Atomic Charge','Active Circuit','Alternating Current'),('03147','Physics','Electricity','What is the unit of electrical resistance?',147,'ilana hadar','Ohm','Ampere','Volt','Watt','Ohm'),('03148','Physics','Electricity','What is the formula for calculating electric charge?',148,'ilana hadar','Q = It','Q = I^2Rt','Q = Pt',' Q = V/R','Q = It');
/*!40000 ALTER TABLE `question` ENABLE KEYS */;
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
