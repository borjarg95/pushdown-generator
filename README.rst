Pushdown Generator
==================


.. image:: https://img.shields.io/github/issues/borjarg95/pushdown-generator.svg
  :alt: Issues on Github
  :target: https://github.com/borjarg95/pushdown-generator/issues

.. image:: https://img.shields.io/github/issues-pr/borjarg95/pushdown-generator.svg
  :alt: Pull Request opened on Github
  :target: https://github.com/borjarg95/pushdown-generator/issues

.. image:: https://img.shields.io/github/release/borjarg95/pushdown-generator.svg
  :alt: Release version on Github
  :target: https://github.com/borjarg95/pushdown-generator/releases/latest

.. image:: https://img.shields.io/github/release-date/borjarg95/pushdown-generator.svg
  :alt: Release date on Github
  :target: https://github.com/borjarg95/pushdown-generator/releases/latest

+-----------------------+--------------------------+----------------------------+-----------------------------+-------------------------------+
| Branch                | Linux Deploy             | Windows Deploy             | CircleCI - Docker           | CodeClimate                   |
+=======================+==========================+============================+=============================+===============================+
|  master               |  |img_ci_travis_status|  |  |img_ci_appveyor_status|  |   |img_ci_circleci_status|  |  |img_ci_codeclimate_status|  |
+-----------------------+--------------------------+----------------------------+-----------------------------+-------------------------------+



Code Metrics by sonarqube
~~~~~~~~~~~~~~~~~~~~~~~~~

.. image:: http://qalab.tk:82/api/badges/gate?key=pushdown-generator
  :alt: Quality Gate
  :target: http://qalab.tk:82/api/badges/gate?key=pushdown-generator
.. image:: http://qalab.tk:82/api/badges/measure?key=pushdown-generator&metric=lines
  :alt: Lines
  :target: http://qalab.tk:82/api/badges/gate?key=pushdown-generator
.. image:: http://qalab.tk:82/api/badges/measure?key=pushdown-generator&metric=bugs
  :alt: Bugs
  :target: http://qalab.tk:82/api/badges/gate?key=pushdown-generator
.. image:: http://qalab.tk:82/api/badges/measure?key=pushdown-generator&metric=vulnerabilities
  :alt: Vulnerabilities
  :target: http://qalab.tk:82/api/badges/gate?key=pushdown-generator
.. image:: http://qalab.tk:82/api/badges/measure?key=pushdown-generator&metric=code_smells
  :alt: Code Smells
  :target: http://qalab.tk:82/api/badges/gate?key=pushdown-generator
.. image:: http://qalab.tk:82/api/badges/measure?key=pushdown-generator&metric=sqale_debt_ratio
  :alt: Debt ratio
  :target: http://qalab.tk:82/api/badges/gate?key=pushdown-generator
.. image:: http://qalab.tk:82/api/badges/measure?key=pushdown-generator&metric=comment_lines_density
  :alt: Comments
  :target: http://qalab.tk:82/api/badges/gate?key=pushdown-generator



Prerequisites
-------------

+ Prerequisites : `install maven for linux/windows/mac`_


Documentation
~~~~~~~~~~~~~

+ How to use library, searching for `Usage Guide`_.

How to start ?
--------------

+ Download binary from: ``wget https://github.com/borjarg95/pushdown-generator/tarball/v1.0.16``
+ Unzip to new folder and enter to directory: ``unzip pushdown-generator-v1.0.16 && cd pushdown-generator-v1.0.16/``
+ Start with binary: ``java -jar pushdown-generator-1.0.16.jar``


How to install ?
----------------

+ Compile java sources: ``mvn install``
+ Execute compiled file (*with MAVEN*): ``mvn spring-boot:run``

How to exec tests ?
-------------------

+ Execute tests (*with MAVEN*) : ``mvn test``
+ Generate tests report (*XML and HTML formats*) : ``mvn surefire-report:report``


Java tested versions
~~~~~~~~~~~~~~~~~~~~

+-----------------------+------------------------+------------------------+
|   **Version Name**    |    **Release Date**    |    **Tests status**    |
+=======================+========================+========================+
|      *openjdk6*       |         *2006*         |     *not supported*    |
+-----------------------+------------------------+------------------------+
|      *openjdk7*       |         *2011*         |     *not supported*    |
+-----------------------+------------------------+------------------------+
|      *openjdk8*       |         *2014*         |       *supported*      |
+-----------------------+------------------------+------------------------+
|      *openjdk9*       |         *2017*         |       *supported*      |
+-----------------------+------------------------+------------------------+
|      *openjdk10*      |         *2017*         |       *supported*      |
+-----------------------+------------------------+------------------------+
|      *openjdk11*      |         *2018*         |     *not supported*    |
+-----------------------+------------------------+------------------------+




MAVEN
~~~~~

*To use maven, need to install first with* ( read more here at: `maven in five minutes`_)


.. _Usage Guide: USAGE.rst
.. _`maven in five minutes`: https://maven.apache.org/guides/getting-started/maven-in-five-minutes.html
.. _`install maven for linux/windows/mac`: http://www.baeldung.com/install-maven-on-windows-linux-mac
.. |img_ci_travis_status| image:: https://travis-ci.org/borjarg95/pushdown-generator.svg?branch=master
    :target: https://travis-ci.org/borjarg95/pushdown-generator?branch=master
.. |img_ci_appveyor_status| image:: https://ci.appveyor.com/api/projects/status/3xxoa0v87svm6l27m16o/branch/master?svg=true 
    :target: https://ci.appveyor.com/api/projects/status/3xxoa0v87svm6l27m16o/branch/master
.. |img_ci_circleci_status| image:: https://circleci.com/gh/borjarg95/pushdown-generator.svg?&style=shield&circle-token=48ff0ecc6afba2cbe94acec8060e8d5115d7da61
    :target: https://circleci.com/gh/borjarg95/pushdown-generator.svg?&style=shield&circle-token=48ff0ecc6afba2cbe94acec8060e8d5115d7da61
.. |img_ci_codeclimate_status| image:: https://api.codeclimate.com/v1/badges/f6443902cc85c540ff3b0abd95bb5045294b5875/maintainability
    :target: https://api.codeclimate.com/v1/badges/f6443902cc85c540ff3b0abd95bb5045294b5875/maintainability
