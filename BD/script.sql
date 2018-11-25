CREATE DATABASE IF NOT EXISTS MUTANTEDB;

USE MUTANTEDB;

DROP TABLE IF EXISTS `User` ;
DROP TABLE IF EXISTS `Mutante_has_Skill` ;
DROP TABLE IF EXISTS `Mutante` ;
DROP TABLE IF EXISTS `Skill` ;

-- -----------------------------------------------------
-- Table `User`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `User` (
  `idUser` INT NOT NULL AUTO_INCREMENT,
  `username` VARCHAR(255) NOT NULL,
  `password` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`idUser`),
  UNIQUE INDEX `username_UNIQUE` (`username` ASC))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `Mutante`
-- -----------------------------------------------------


CREATE TABLE IF NOT EXISTS `Mutante` (
  `idMutante` INT NOT NULL AUTO_INCREMENT,
  `mutanteName` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`idMutante`),
  UNIQUE INDEX `mutanteName_UNIQUE` (`mutanteName` ASC))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `Skill`
-- -----------------------------------------------------


CREATE TABLE IF NOT EXISTS `Skill` (
  `idSkill` INT NOT NULL AUTO_INCREMENT,
  `skillName` VARCHAR(255) NULL,
  PRIMARY KEY (`idSkill`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `Mutante_has_Skill`
-- -----------------------------------------------------


CREATE TABLE IF NOT EXISTS `Mutante_has_Skill` (
  `Mutante_idMutante` INT NOT NULL,
  `Skill_idSkill` INT NOT NULL,
  PRIMARY KEY (`Mutante_idMutante`, `Skill_idSkill`),
  INDEX `fk_Mutante_has_Skill_Skill1_idx` (`Skill_idSkill` ASC),
  INDEX `fk_Mutante_has_Skill_Mutante_idx` (`Mutante_idMutante` ASC),
  CONSTRAINT `fk_Mutante_has_Skill_Mutante`
    FOREIGN KEY (`Mutante_idMutante`)
    REFERENCES `Mutante` (`idMutante`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Mutante_has_Skill_Skill1`
    FOREIGN KEY (`Skill_idSkill`)
    REFERENCES `Skill` (`idSkill`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;