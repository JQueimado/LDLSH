#! /bin/bash
./run-quinta-servers.sh all -k
./run-test-quinta.sh . 10 SRR10000shufaa SRR10000shufaa > log_SRRaa_SRRaa_100.txt 2>&1;
./run-test-quinta.sh . 10 SRR10000shufaa SRR10000shufab > log_SRRaa_SRRab_100.txt 2>&1;
./run-test-quinta.sh . 10 SRR10000shufaa SRR10000shufac > log_SRRaa_SRRaw_100.txt 2>&1;

./run-test-quinta.sh . 10 SRR100000shufaa SRR100000shufaa > log_SRRaa_SRRaa_100.txt 2>&1;
./run-test-quinta.sh . 10 SRR100000shufaa SRR100000shufab > log_SRRaa_SRRab_100.txt 2>&1;
./run-test-quinta.sh . 10 SRR100000shufaa SRR100000shufac > log_SRRaa_SRRaw_100.txt 2>&1;