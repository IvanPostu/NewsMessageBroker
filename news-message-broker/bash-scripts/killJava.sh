#!/bin/sh


wmic process where "name like '%java%'" delete

