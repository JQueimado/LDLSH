#! /bin/bash

./run-quinta-servers.sh all -k
rm -rf TESTS* log_*

./run-test-quinta.sh . 10 SRR_2400000_aa SRR_2400000_aa > log_SRRaa_SRRaa.txt 2>&1;
./run-test-quinta.sh . 10 SRR_2400000_aa SRR_2400000_ab > log_SRRaa_SRRab.txt 2>&1;
./run-test-quinta.sh . 10 SRR_2400000_aa SRR_2400000_ac > log_SRRaa_SRRac.txt 2>&1;
./run-test-quinta.sh . 10 SRR_2400000_aa SRR_2400000_aw > log_SRRaa_SRRaw.txt 2>&1;
./run-test-quinta.sh . 10 SRR_2400000_aw SRR_2400000_aa > log_SRRaw_SRRaa.txt 2>&1;
./run-test-quinta.sh . 10 SRR_2400000_aw SRR_2400000_ax > log_SRRaw_SRRax.txt 2>&1;
./run-test-quinta.sh . 10 SRR_2400000_aw SRR_2400000_ay > log_SRRaw_SRRay.txt 2>&1;
./run-test-quinta.sh . 10 SRR_2400000_aw SRR_2400000_az > log_SRRaw_SRRaz.txt 2>&1;
./run-test-quinta.sh . 10 SRR_2400000_aw SRR_2400000_br > log_SRRaw_SRRaz.txt 2>&1;
./run-test-quinta.sh . 10 SRR_2400000_br SRR_2400000_aa > log_SRRbr_SRRaa.txt 2>&1;
./run-test-quinta.sh . 10 SRR_2400000_br SRR_2400000_aw > log_SRRbr_SRRaw.txt 2>&1;
./run-test-quinta.sh . 10 SRR_2400000_br SRR_2400000_bs > log_SRRbr_SRRbs.txt 2>&1;
