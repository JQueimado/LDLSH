#! /bin/bash
./run-quinta-servers.sh all -k
./run-test-quinta.sh . 10 SRR10000shufaa_x10.txt SRR10000shufaa_x10.txt > log_SRRaa_SRRaa_10x10.txt 2>&1;
./run-test-quinta.sh . 10 SRR10000shufaa_x10.txt SRR10000shufab_x10.txt > log_SRRaa_SRRab_10x10.txt 2>&1;
./run-test-quinta.sh . 10 SRR10000shufaa_x10.txt SRR10000shufac_x10.txt > log_SRRaa_SRRaw_10x10.txt 2>&1;