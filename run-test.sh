#! /bin/bash

./run-quinta-servers.sh all -k
rm -rf TESTS* log_*

./run-test-quinta.sh . 10 SRR_2400000_aa SRR_2400000_aa > log_SRRaa_SRRaa.txt 2>&1;
./run-test-quinta.sh . 10 SRR_2400000_aa SRR_2400000_ab > log_SRRaa_SRRab.txt 2>&1;
./run-test-quinta.sh . 10 SRR_2400000_aa SRR_2400000_aw > log_SRRaa_SRRaw.txt 2>&1;
