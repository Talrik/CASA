<?php
/**
 * This file contains the basic migration functionality
 *
 * Copyright (c)  2013  <philipp.lehsten@gmail.com>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 * @category   StudIP_Plugin
 * @package    de.lehsten.casa.studip.plugin
 * @author     Philipp Lehsten <philipp.lehsten@uni-rostock.de>
 * @copyright  2013 Philipp Lehsten <philipp.lehsten@uni-rostock.de>
 * @since      File available since Release 1.0
 */

/**
 *
 * This class is the basic migration class.
 * Others might follow...
 * 
 * @author  Philipp Lehsten <philipp.lehsten@uni-rostock.de>
 */	
class CreateDatabase extends DBMigration
{

	/**
	 * returns the description 
	 */	
    function description ()
    {
        return 'Creates neccessary tables for CASA plugin';
    }

	/**
	 * creates the table 
	 */	
    function up ()
    {
        DBManager::get()->exec("CREATE TABLE IF NOT EXISTS `casa_services` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(64) NOT NULL,
  `description` text,
  `url` varchar(255) NOT NULL,
  `userrole` varchar(64) NOT NULL,
  `location` varchar(255) DEFAULT NULL,
  `lecture` varchar(64) DEFAULT NULL,
  `serviceXML` text NOT NULL,
  `timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified` DATETIME NOT NULL,
  PRIMARY KEY (`id`)
)");
    }


	/**
	 * drops the table 
	 */	
    function down ()
    {
        DBManager::get()->exec("DROP TABLE IF EXISTS `casa_services`");
    }
}