#! /bin/bash

./run-quinta-servers.sh all -k
rm -rf TESTS* log_*

#./run-test-quinta.sh LDLSH 5 SRR_100I_aa SRR_100I_aa #> log_SRRaa_SRRaa_L.txt 2>&1;
#./run-test-quinta.sh LDLDH 5 SRR_100I_aa SRR_100000000_Q_ab > log_SRRaa_SRRab_L.txt 2>&1;
#./run-test-quinta.sh LDLSH 5 SRR_100I_aa SRR_100000000_Q_ac > log_SRRaa_SRRac_L.txt 2>&1;

./run-test-quinta.sh . 10 SRR_100I_aa SRR_100I_aa > log_SRRaa_SRRaa.txt 2>&1;
./run-test-quinta.sh . 10 SRR_100I_aa SRR_100I_ab > log_SRRaa_SRRab.txt 2>&1;
./run-test-quinta.sh . 10 SRR_100I_aa SRR_100I_ac > log_SRRaa_SRRac.txt 2>&1;
./run-test-quinta.sh . 10 SRR_100I_aa SRR_100I_aw > log_SRRaa_SRRaw.txt 2>&1;
./run-test-quinta.sh . 10 SRR_100I_aw SRR_100I_aa > log_SRRaw_SRRaa.txt 2>&1;
./run-test-quinta.sh . 10 SRR_100I_aw SRR_100I_ax > log_SRRaw_SRRax.txt 2>&1;
./run-test-quinta.sh . 10 SRR_100I_aw SRR_100I_ay > log_SRRaw_SRRay.txt 2>&1;
./run-test-quinta.sh . 10 SRR_100I_aw SRR_100I_az > log_SRRaw_SRRaz.txt 2>&1;
./run-test-quinta.sh . 10 SRR_100I_br SRR_100I_aa > log_SRRbr_SRRaa.txt 2>&1;
./run-test-quinta.sh . 10 SRR_100I_br SRR_100I_aw > log_SRRbr_SRRaw.txt 2>&1;
./run-test-quinta.sh . 10 SRR_100I_br SRR_100I_bs > log_SRRbr_SRRbs.txt 2>&1;

#./run-test-quinta.sh 5 SRR_100I_aa SRR_100000000_Q_aa > log_SRRaa_SRRaa_L.txt 2>&1;
#./run-test-quinta.sh 5 SRR_100I_aa SRR_100000000_Q_ab > log_SRRaa_SRRab_L.txt 2>&1;
#./run-test-quinta.sh 5 SRR_100I_aa SRR_100000000_Q_ac > log_SRRaa_SRRac_L.txt 2>&1;