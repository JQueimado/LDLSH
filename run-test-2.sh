#! /bin/bash
./run-quinta-servers.sh all -k
./run-test-quinta.sh . 10 SRR100000shufaax10 SRR100000shufaax10 > log_SRRaa_SRRaa_10x10.txt 2>&1;
./run-test-quinta.sh . 10 SRR100000shufaax10 SRR100000shufabx10 > log_SRRaa_SRRab_10x10.txt 2>&1;
./run-test-quinta.sh . 10 SRR100000shufaax10 SRR100000shufacx10 > log_SRRaa_SRRaw_10x10.txt 2>&1;