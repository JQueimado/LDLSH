#!/usr/bin/gnuplot
reset

set size ratio 0.65
set xrange [0.74:0.96]

set logscale y 2

set lmargin 0.3
set rmargin 0.3

A = "#2F528F"
B = "#ED7D31"
C = "#70AD47"
D = "#A5A5A5"
F = '#000'

#data-set similarities
# DS10n3 = 0.7937854073276378
# DS10n4 = 0.8495047623629264	
# DS10x10n3 = 0.8470108839664949
# DS10x10n4 = 0.9138432553377478

set style line 1 lt rgb A lw 7 pt 9 ps 3 pi -2
set style line 2 lt rgb A lw 7 pt 9 ps 3 pi -2
set style line 3 lt rgb B lw 7 pt 7 ps 3 pi -2
set style line 4 lt rgb B lw 7 pt 7 ps 3 pi -2
set style line 5 lt rgb C lw 7 pt 5 ps 3 pi -2
set style line 6 lt rgb C lw 7 pt 5 ps 3 pi -2
set style line 7 lt rgb D lw 7 pt 43 ps 3 pi -2
set style line 8 lt rgb D lw 7 pt 43 ps 3 pi -2
set style line 9 lt rgb F lw 7 dt 2

set terminal pdf color font ",60" size 18, 10
set size ratio .55 1.2,1

###############################################################################################################################

## Accuracy ##
set logscale y 2
set yrange [0.96:0.999]
set ytics ( "0.79" .962,0.97,0.98,0.99,1)

set xlabel "Similarity Treshold" 
set ylabel "Average distance"

set key at 0.95,0.98

set out "TestThresholdxAccuracy_10-10.pdf"
plot 'TestThresholdxAccuracy_10-10.csv' index 0 using 4:1 title "LDLSH Optimized" with linespoints ls 2, \
		'' index 0 using 4:1:2:3 title '' with errorbars ls 1,\
		'' index 1 using 4:1 title "LDLSH" with linespoints ls 4, \
		'' index 1 using 4:1:2:3 title '' with errorbars ls 3, \
 		'' index 2 using 4:1 title "Traditional Replicated" with linespoints ls 6, \
		'' index 2 using 4:1:2:3 title '' with errorbars ls 5,\
		'' index 3 using 4:1 title "Traditional External" with linespoints ls 8, \
		'' index 3 using 4:1:2:3 title '' with errorbars ls 7, \
		.962 title 'data-set baseline' ls 9

set key default

set yrange [0.975:1.001]
set ytics ( "0.85" .978,0.97,0.98,0.99,1)

##

set xlabel "Similarity Treshold" 
set ylabel "Average distance"

set key at 0.95,0.99

set out "TestThresholdxAccuracy_10x10-10x10.pdf"
plot 'TestThresholdxAccuracy_10x10-10x10.csv' index 0 using 4:1 title "LDLSH Optimized" with linespoints ls 2, \
		'' index 0 using 4:1:2:3 title '' with errorbars ls 1,\
		'' index 1 using 4:1 title "LDLSH" with linespoints ls 4, \
		'' index 1 using 4:1:2:3 title '' with errorbars ls 3, \
 		'' index 2 using 4:1 title "Traditional Replicated" with linespoints ls 6, \
		'' index 2 using 4:1:2:3 title '' with errorbars ls 5,\
		'' index 3 using 4:1 title "Traditional External" with linespoints ls 8, \
		'' index 3 using 4:1:2:3 title '' with errorbars ls 7, \
		.978 title 'data-set baseline' ls 9

set key default


##### TEST 10_10 #####
#### a-a ####
unset ytics
unset xtics

set xtics(0.75,0.9,0.95)

### ThresholdxLatency_10a-10a.pdf ###
set yrange [6:35]
set ytics (10,20,30,35)

#set title "Average latency in relation to the similarity threshold for data-set 10a-10a"
set xlabel "Similarity Treshold" 
set ylabel "Average Latency"

set out "TestThresholdxLatency_10a-10a.pdf"
plot 'TestThresholdxLatency_10a-10a.csv' index 0 using 4:1:2:3 title "LDLSH Optimized" with errorbars ls 2, \
		'' index 0 using 4:1 title '' with linespoints ls 1, \
		'' index 1 using 4:1:2:3 title "LDLSH" with errorbars ls 4, \
		'' index 1 using 4:1 title '' with linespoints ls 3, \
		'' index 2 using 4:1:2:3 title "Traditional Replicated" with errorbars ls 6, \
		'' index 2 using 4:1 title '' with linespoints ls 5, \
		'' index 3 using 4:1:2:3 title "Traditional External" with errorbars ls 8, \
		'' index 3 using 4:1 title '' with linespoints ls 7

### TestThresholdxAccuracy_10a-10a.pdf ###
#set yrange [0.0005:0.0025]
#set ytics (0.0005,0.001,0.002,0.0025)

##set title "Average accuracy in relation to the similarity threshold for data-set 10a-10a"
#set xlabel "Similarity Treshold" 
#set ylabel "Average distance"

#set out "TestThresholdxAccuracy_10a-10a.pdf"
#plot 'TestThresholdxAccuracy_10a-10a.csv' index 0 using 2:1 title "LDLSH Optimized" with linespoints ls 2, \
#		'' index 0 using 2:1 title '' ls 1,\
#		'' index 1 using 2:1 title "LDLSH" with linespoints ls 4, \
#		'' index 1 using 2:1 title '' ls 3, \
# 		'' index 2 using 2:1 title "Traditional Replicated" with linespoints ls 6, \
#		'' index 2 using 2:1 title '' ls 5,\
#		'' index 3 using 2:1 title "Traditional External" with linespoints ls 8, \
#		'' index 3 using 2:1 title '' ls 7

### TestThresholdxThroughput_10a-10a.pdf ###
set yrange [340:2600]
set ytics (340,500,1000,1500,2000,25000,2600)

#set title "Average throughput in relation to the similarity threshold for data-set 10a-10a"
set xlabel "Similarity Treshold" 
set ylabel "Average Throughput"

set key right bottom 

set out "TestThresholdxThroughput_10a-10a.pdf"
plot 'TestThresholdxThroughput_10a-10a.csv' index 0 using 4:1:2:3 title "LDLSH Optimized" with errorbars ls 2, \
		'' index 0 using 4:1 title '' with linespoints ls 1, \
		'' index 1 using 4:1:2:3 title "LDLSH" with errorbars ls 4, \
		'' index 1 using 4:1 title '' with linespoints ls 3, \
		'' index 2 using 4:1:2:3 title "Traditional Replicated" with errorbars ls 6, \
		'' index 2 using 4:1 title '' with linespoints ls 5, \
		'' index 3 using 4:1:2:3 title "Traditional External" with errorbars ls 8, \
		'' index 3 using 4:1 title '' with linespoints ls 7

###############################################################################################################################

#### a-b ####
### ThresholdxLatency_10a-10b.pdf ###
# set yrange [6:30]
# set ytics (5,10,20,30)

# #set title "Average latency in relation to the similarity threshold for data-set 10a-10b"
# set xlabel "Similarity Treshold" 
# set ylabel "Average Latency"

# set key right top 

# set out "TestThresholdxLatency_10a-10b.pdf"
# plot 'TestThresholdxLatency_10a-10b.csv' index 0 using 4:1:2:3 title "LDLSH Optimized" with errorbars ls 2, \
# 		'' index 0 using 4:1 title '' with linespoints ls 1, \
# 		'' index 1 using 4:1:2:3 title "LDLSH" with errorbars ls 4, \
# 		'' index 1 using 4:1 title '' with linespoints ls 3, \
# 		'' index 2 using 4:1:2:3 title "Traditional Replicated" with errorbars ls 6, \
# 		'' index 2 using 4:1 title '' with linespoints ls 5, \
# 		'' index 3 using 4:1:2:3 title "Traditional External" with errorbars ls 8, \
# 		'' index 3 using 4:1 title '' with linespoints ls 7

## Latency 10-10 Query
set yrange [6:30]
set ytics (5,10,20,30)

set xlabel "Similarity Treshold" 
set ylabel "Average Latency"

set key right top 

set out "TestThresholdxLatency_10-10.pdf"
plot 'TestThresholdxLatency_10-10.csv' index 0 using 4:1:2:3 title "LDLSH Optimized" with errorbars ls 2, \
		'' index 0 using 4:1 title '' with linespoints ls 1, \
		'' index 1 using 4:1:2:3 title "LDLSH" with errorbars ls 4, \
		'' index 1 using 4:1 title '' with linespoints ls 3, \
		'' index 2 using 4:1:2:3 title "Traditional Replicated" with errorbars ls 6, \
		'' index 2 using 4:1 title '' with linespoints ls 5, \
		'' index 3 using 4:1:2:3 title "Traditional External" with errorbars ls 8, \
		'' index 3 using 4:1 title '' with linespoints ls 7

## Latency 10-10 Insert
set yrange [7:15]
set ytics (1)

set xlabel "Similarity Treshold" 
set ylabel "Average Latency"

set key right top 

set out "TestThresholdxLatency_insert_10-10.pdf"
plot 'TestThresholdxLatency_insert_10-10.csv' index 0 using 2:1 title "LDLSH Optimized" with linespoints ls 2, \
		'' index 0 using 2:1 title '' ls 1, \
		'' index 1 using 2:1 title "LDLSH" with linespoints ls 3, \
		'' index 1 using 2:1 title '' ls 4, \
		'' index 2 using 2:1 title "Traditional Replicated" with linespoints ls 6, \
		'' index 2 using 2:1 title '' ls 5, \
		'' index 3 using 2:1 title "Traditional External" with linespoints ls 8, \
		'' index 3 using 2:1 title '' ls 7

### TestThresholdxAccuracy_10a-10b.pdf ###
# set logscale y 2
# set yrange [0.96:0.999]
# set ytics ( "0.79" .962,0.97,0.98,0.99)

#set title "Average accuracy in relation to the similarity threshold for data-set 10a-10b"
# set xlabel "Similarity Treshold" 
# set ylabel "Average distance"

# set key left top 

# set out "TestThresholdxAccuracy_10a-10b.pdf"
# plot 'TestThresholdxAccuracy_10a-10b.csv' index 0 using 4:1 title "LDLSH Optimized" with linespoints ls 2, \
# 		'' index 0 using 4:1:2:3 title '' with errorbars ls 1,\
# 		'' index 1 using 4:1 title "LDLSH" with linespoints ls 4, \
# 		'' index 1 using 4:1:2:3 title '' with errorbars ls 3, \
#  		'' index 2 using 4:1 title "Traditional Replicated" with linespoints ls 6, \
# 		'' index 2 using 4:1:2:3 title '' with errorbars ls 5,\
# 		'' index 3 using 4:1 title "Traditional External" with linespoints ls 8, \
# 		'' index 3 using 4:1:2:3 title '' with errorbars ls 7, \
# 		.962 title 'dataset similarity' ls 9

### TestThresholdxThroughput_10a-10b.pdf ###
set yrange [390:2800]
set ytics (500,1000,1500,2000,25000,3000,3500)

#set title "Average throughput in relation to the similarity threshold for data-set 10a-10b"
set xlabel "Similarity Treshold" 
set ylabel "Average Throughput"

set key right bottom 

set out "TestThresholdxThroughput_10a-10b.pdf"
plot 'TestThresholdxThroughput_10a-10b.csv' index 0 using 4:1:2:3 title "LDLSH Optimized" with errorbars ls 2, \
		'' index 0 using 4:1 title '' with linespoints ls 1, \
		'' index 1 using 4:1:2:3 title "LDLSH" with errorbars ls 4, \
		'' index 1 using 4:1 title '' with linespoints ls 3, \
		'' index 2 using 4:1:2:3 title "Traditional Replicated" with errorbars ls 6, \
		'' index 2 using 4:1 title '' with linespoints ls 5, \
		'' index 3 using 4:1:2:3 title "Traditional External" with errorbars ls 8, \
		'' index 3 using 4:1 title '' with linespoints ls 7

###############################################################################################################################

#### a-c ####
### ThresholdxLatency_10a-10c.pdf ###
set yrange [6:31]
set ytics (5,10,20,30)

#set title "Average latency in relation to the similarity threshold for data-set 10a-10c"
set xlabel "Similarity Treshold" 
set ylabel "Average Latency"

set key right top 

set out "TestThresholdxLatency_10a-10c.pdf"
plot 'TestThresholdxLatency_10a-10c.csv' index 0 using 4:1:2:3 title "LDLSH Optimized" with errorbars ls 2, \
		'' index 0 using 4:1 title '' with linespoints ls 1, \
		'' index 1 using 4:1:2:3 title "LDLSH" with errorbars ls 4, \
		'' index 1 using 4:1 title '' with linespoints ls 3, \
		'' index 2 using 4:1:2:3 title "Traditional Replicated" with errorbars ls 6, \
		'' index 2 using 4:1 title '' with linespoints ls 5, \
		'' index 3 using 4:1:2:3 title "Traditional External" with errorbars ls 8, \
		'' index 3 using 4:1 title '' with linespoints ls 7

### TestThresholdxAccuracy_10a-10c.pdf ###
# set yrange [0.96:0.999]
# set ytics ( "0.79" .962,0.97,0.98,0.99)

# #set title "Average accuracy in relation to the similarity threshold for data-set 10a-10c"
# set xlabel "Similarity Treshold" 
# set ylabel "Average distance"

# set key left top 

# set out "TestThresholdxAccuracy_10a-10c.pdf"
# plot 'TestThresholdxAccuracy_10a-10c.csv' index 0 using 4:1 title "LDLSH Optimized" with linespoints ls 2, \
# 		'' index 0 using 4:1:2:3 title '' with errorbars ls 1,\
# 		'' index 1 using 4:1 title "LDLSH" with linespoints ls 4, \
# 		'' index 1 using 4:1:2:3 title '' with errorbars ls 3, \
#  		'' index 2 using 4:1 title "Traditional Replicated" with linespoints ls 6, \
# 		'' index 2 using 4:1:2:3 title '' with errorbars ls 5,\
# 		'' index 3 using 4:1 title "Traditional External" with linespoints ls 8, \
# 		'' index 3 using 4:1:2:3 title '' with errorbars ls 7, \
# 		.962 title 'dataset similarity' ls 9


### TestThresholdxThroughput_10a-10c.pdf ###
set yrange [390:2800]
set ytics (500,1000,1500,2000,25000,3000,3500)

#set title "Average throughput in relation to the similarity threshold for data-set 10a-10c"
set xlabel "Similarity Treshold" 
set ylabel "Average Throughput"

set key right bottom 

set out "TestThresholdxThroughput_10a-10c.pdf"
plot 'TestThresholdxThroughput_10a-10c.csv' index 0 using 4:1:2:3 title "LDLSH Optimized" with errorbars ls 2, \
		'' index 0 using 4:1 title '' with linespoints ls 1, \
		'' index 1 using 4:1:2:3 title "LDLSH" with errorbars ls 4, \
		'' index 1 using 4:1 title '' with linespoints ls 3, \
		'' index 2 using 4:1:2:3 title "Traditional Replicated" with errorbars ls 6, \
		'' index 2 using 4:1 title '' with linespoints ls 5, \
		'' index 3 using 4:1:2:3 title "Traditional External" with errorbars ls 8, \
		'' index 3 using 4:1 title '' with linespoints ls 7

###############################################################################################################################

##### TEST 10x10_10x10 #####
#### a-a ####
### ThresholdxLatency_10x10a-10x10a.pdf ###
set yrange [10:30]
set ytics (5,10,20,30)

#set title "Average latency in relation to the similarity threshold for data-set 10x10a-10x10a"
set xlabel "Similarity Treshold" 
set ylabel "Average Latency"

set key right top 

set out "TestThresholdxLatency_10x10a-10x10a.pdf"
plot 'TestThresholdxLatency_10x10a-10x10a.csv' index 0 using 4:1:2:3 title "LDLSH Optimized" with errorbars ls 2, \
		'' index 0 using 4:1 title '' with linespoints ls 1, \
		'' index 1 using 4:1:2:3 title "LDLSH" with errorbars ls 4, \
		'' index 1 using 4:1 title '' with linespoints ls 3, \
		'' index 2 using 4:1:2:3 title "Traditional Replicated" with errorbars ls 6, \
		'' index 2 using 4:1 title '' with linespoints ls 5, \
		'' index 3 using 4:1:2:3 title "Traditional External" with errorbars ls 8, \
		'' index 3 using 4:1 title '' with linespoints ls 7

### TestThresholdxAccuracy_10x10a-10x10a.pdf ###
#set yrange [0.96:0.999]
#set ytics (0.96,0.97,0.98,0.99)

##set title "Average accuracy in relation to the similarity threshold for data-set 10x10a-10x10a"
#set xlabel "Similarity Treshold" 
#set ylabel "Average distance"

#set key right bottom 

#set out "TestThresholdxAccuracy_10x10a-10x10a.pdf"
#plot 'TestThresholdxAccuracy_10x10a-10x10a.csv' index 0 using 2:1 title "LDLSH Optimized" with linespoints ls 2, \
#		'' index 0 using 2:1 title '' ls 1,\
#		'' index 1 using 2:1 title "LDLSH" with linespoints ls 4, \
#		'' index 1 using 2:1 title '' ls 3, \
#		'' index 2 using 2:1 title "Traditional Replicated" with linespoints ls 6, \
#		'' index 2 using 2:1 title '' ls 5,\
#		'' index 3 using 2:1 title "Traditional External" with linespoints ls 8, \
#		'' index 3 using 2:1 title '' ls 7


### TestThresholdxThroughput_10x10a-10x10a.pdf ###
set yrange [430:1800]
set ytics (500,1000,1500,2000,25000,3000,3500)

#set title "Average throughput in relation to the similarity threshold for data-set 10x10a-10x10a"
set xlabel "Similarity Treshold" 
set ylabel "Average Throughput"

set key right bottom 

set out "TestThresholdxThroughput_10x10a-10x10a.pdf"
plot 'TestThresholdxThroughput_10x10a-10x10a.csv' index 0 using 4:1:2:3 title "LDLSH Optimized" with errorbars ls 2, \
		'' index 0 using 4:1 title '' with linespoints ls 1, \
		'' index 1 using 4:1:2:3 title "LDLSH" with errorbars ls 4, \
		'' index 1 using 4:1 title '' with linespoints ls 3, \
		'' index 2 using 4:1:2:3 title "Traditional Replicated" with errorbars ls 6, \
		'' index 2 using 4:1 title '' with linespoints ls 5, \
		'' index 3 using 4:1:2:3 title "Traditional External" with errorbars ls 8, \
		'' index 3 using 4:1 title '' with linespoints ls 7

###############################################################################################################################

#### a-b ####
### ThresholdxLatency_10x10a-10x10b.pdf ###
set yrange [7:28]
set ytics (5,10,20,30)

#set title "Average latency in relation to the similarity threshold for data-set 10x10a-10x10b"
set xlabel "Similarity Treshold" 
set ylabel "Average Latency"

set key right top 

set out "TestThresholdxLatency_10x10a-10x10b.pdf"
plot 'TestThresholdxLatency_10x10a-10x10b.csv' index 0 using 4:1:2:3 title "LDLSH Optimized" with errorbars ls 2, \
		'' index 0 using 4:1 title '' with linespoints ls 1, \
		'' index 1 using 4:1:2:3 title "LDLSH" with errorbars ls 4, \
		'' index 1 using 4:1 title '' with linespoints ls 3, \
		'' index 2 using 4:1:2:3 title "Traditional Replicated" with errorbars ls 6, \
		'' index 2 using 4:1 title '' with linespoints ls 5, \
		'' index 3 using 4:1:2:3 title "Traditional External" with errorbars ls 8, \
		'' index 3 using 4:1 title '' with linespoints ls 7

### TestThresholdxAccuracy_10x10a-10x10b.pdf ###
# set yrange [0.975:1.001]
# set ytics ( "0.85" .978,0.97,0.98,0.99)

# #set title "Average accuracy in relation to the similarity threshold for data-set 10x10a-10x10b"
# set xlabel "Similarity Treshold" 
# set ylabel "Average distance"

# set key left top 

# set out "TestThresholdxAccuracy_10x10a-10x10b.pdf"
# plot 'TestThresholdxAccuracy_10x10a-10x10b.csv' index 0 using 4:1 title "LDLSH Optimized" with linespoints ls 2, \
# 		'' index 0 using 4:1:2:3 title '' with errorbars ls 1,\
# 		'' index 1 using 4:1 title "LDLSH" with linespoints ls 4, \
# 		'' index 1 using 4:1:2:3 title '' with errorbars ls 3, \
#  		'' index 2 using 4:1 title "Traditional Replicated" with linespoints ls 6, \
# 		'' index 2 using 4:1:2:3 title '' with errorbars ls 5,\
# 		'' index 3 using 4:1 title "Traditional External" with linespoints ls 8, \
# 		'' index 3 using 4:1:2:3 title '' with errorbars ls 7, \
# 		.978 title 'dataset similarity' ls 9

### TestThresholdxThroughput_10x10a-10x10b.pdf ###
set yrange [460:2500]
set ytics (500,1000,1500,2000,25000,3000,3500)

#set title "Average throughput in relation to the similarity threshold for data-set 10x10a-10x10b"
set xlabel "Similarity Treshold" 
set ylabel "Average Throughput"

set key right bottom 

set out "TestThresholdxThroughput_10x10a-10x10b.pdf"
plot 'TestThresholdxThroughput_10x10a-10x10b.csv' index 0 using 4:1:2:3 title "LDLSH Optimized" with errorbars ls 2, \
		'' index 0 using 4:1 title '' with linespoints ls 1, \
		'' index 1 using 4:1:2:3 title "LDLSH" with errorbars ls 4, \
		'' index 1 using 4:1 title '' with linespoints ls 3, \
		'' index 2 using 4:1:2:3 title "Traditional Replicated" with errorbars ls 6, \
		'' index 2 using 4:1 title '' with linespoints ls 5, \
		'' index 3 using 4:1:2:3 title "Traditional External" with errorbars ls 8, \
		'' index 3 using 4:1 title '' with linespoints ls 7

###############################################################################################################################

#### a-c ####
### ThresholdxLatency_10x10a-10x10c.pdf ###
set yrange [7.5:27]
set ytics (5,10,20,30)

#set title "Average latency in relation to the similarity threshold for data-set 10x10a-10x10c"
set xlabel "Similarity Treshold" 
set ylabel "Average Latency"

set key right top 

set out "TestThresholdxLatency_10x10a-10x10c.pdf"
plot 'TestThresholdxLatency_10x10a-10x10c.csv' index 0 using 4:1:2:3 title "LDLSH Optimized" with errorbars ls 2, \
		'' index 0 using 4:1 title '' with linespoints ls 1, \
		'' index 1 using 4:1:2:3 title "LDLSH" with errorbars ls 4, \
		'' index 1 using 4:1 title '' with linespoints ls 3, \
		'' index 2 using 4:1:2:3 title "Traditional Replicated" with errorbars ls 6, \
		'' index 2 using 4:1 title '' with linespoints ls 5, \
		'' index 3 using 4:1:2:3 title "Traditional External" with errorbars ls 8, \
		'' index 3 using 4:1 title '' with linespoints ls 7

### TestThresholdxAccuracy_10x10a-10x10c.pdf ###
# set yrange [0.975:1.001]
# set ytics ( "0.85" .978,0.97,0.98,0.99)

# #set title "Average accuracy in relation to the similarity threshold for data-set 10x10a-10x10c"
# set xlabel "Similarity Treshold" 
# set ylabel "Average distance"

# set key left top 

# set out "TestThresholdxAccuracy_10x10a-10x10c.pdf"
# plot 'TestThresholdxAccuracy_10x10a-10x10c.csv' index 0 using 4:1 title "LDLSH Optimized" with linespoints ls 2, \
# 		'' index 0 using 4:1:2:3 title '' with errorbars ls 1,\
# 		'' index 1 using 4:1 title "LDLSH" with linespoints ls 4, \
# 		'' index 1 using 4:1:2:3 title '' with errorbars ls 3, \
#  		'' index 2 using 4:1 title "Traditional Replicated" with linespoints ls 6, \
# 		'' index 2 using 4:1:2:3 title '' with errorbars ls 5,\
# 		'' index 3 using 4:1 title "Traditional External" with linespoints ls 8, \
# 		'' index 3 using 4:1:2:3 title '' with errorbars ls 7, \
# 		.978 title 'dataset similarity' ls 9


### TestThresholdxThroughput_10x10a-10x10c.pdf ###
set yrange [500:2500]
set ytics (500,1000,1500,2000,25000,3000,3500)

#set title "Average throughput in relation to the similarity threshold for data-set 10x10a-10x10c"
set xlabel "Similarity Treshold" 
set ylabel "Average Throughput"

set key right bottom 

set out "TestThresholdxThroughput_10x10a-10x10c.pdf"
plot 'TestThresholdxThroughput_10x10a-10x10c.csv' index 0 using 4:1:2:3 title "LDLSH Optimized" with errorbars ls 2, \
		'' index 0 using 4:1 title '' with linespoints ls 1, \
		'' index 1 using 4:1:2:3 title "LDLSH" with errorbars ls 4, \
		'' index 1 using 4:1 title '' with linespoints ls 3, \
		'' index 2 using 4:1:2:3 title "Traditional Replicated" with errorbars ls 6, \
		'' index 2 using 4:1 title '' with linespoints ls 5, \
		'' index 3 using 4:1:2:3 title "Traditional External" with errorbars ls 8, \
		'' index 3 using 4:1 title '' with linespoints ls 7

###############################################################################################################################

##### TEST 100_100 #####
#### a-a ####
### ThresholdxLatency_100a-100a.pdf ###
set yrange [4:250]
set ytics (5,10,20,30,40,50,70,90,110,150,200, 250)

#set title "Average latency in relation to the similarity threshold for data-set 100a-100a"
set xlabel "Similarity Treshold" 
set ylabel "Average Latency"

set key right top 

set out "TestThresholdxLatency_100a-100a.pdf"
plot 'TestThresholdxLatency_100a-100a.csv' index 0 using 4:1:2:3 title "LDLSH Optimized" with errorbars ls 2, \
		'' index 0 using 4:1 title '' with linespoints ls 1, \
		'' index 1 using 4:1:2:3 title "LDLSH" with errorbars ls 4, \
		'' index 1 using 4:1 title '' with linespoints ls 3, \
		'' index 2 using 4:1:2:3 title "Traditional Replicated" with errorbars ls 6, \
		'' index 2 using 4:1 title '' with linespoints ls 5, \
		'' index 3 using 4:1:2:3 title "Traditional External" with errorbars ls 8, \
		'' index 3 using 4:1 title '' with linespoints ls 7

### TestThresholdxAccuracy_100a-100a.pdf ###
#set yrange [0.0012:0.0031]
#set ytics (0.0012,0.0015,0.002,0.0025,0.003)

#set title "Average accuracy in relation to the similarity threshold for data-set 100a-100a"
#set xlabel "Similarity Treshold" 
#set ylabel "Average distance"

#set key right top 

#set out "TestThresholdxAccuracy_100a-100a.pdf"
#plot 'TestThresholdxAccuracy_100a-100a.csv' index 0 using 2:1 title "LDLSH Optimized" with linespoints ls 2, \
#		'' index 0 using 2:1 title '' ls 1,\
#		'' index 1 using 2:1 title "LDLSH" with linespoints ls 4, \
#		'' index 1 using 2:1 title '' ls 3, \
#		'' index 2 using 2:1 title "Traditional Replicated" with linespoints ls 6, \
#		'' index 2 using 2:1 title '' ls 5,\
#		'' index 3 using 2:1 title "Traditional External" with linespoints ls 8, \
#		'' index 3 using 2:1 title '' ls 7


### TestThresholdxThroughput_100a-100a.pdf ###
#set yrange [-420:3600]
unset yrange
unset logscale
#set ytics (500,1000,1500,2000,25000,3000,3500)
set ytics 500

#set title "Average throughput in relation to the similarity threshold for data-set 100a-100a"
set xlabel "Similarity Treshold" 
set ylabel "Average Throughput"

set key right bottom 

set out "TestThresholdxThroughput_100a-100a.pdf"
plot 'TestThresholdxThroughput_100a-100a.csv' index 0 using 4:1:2:3 title "LDLSH Optimized" with errorbars ls 2, \
		'' index 0 using 4:1 title '' with linespoints ls 1, \
		'' index 1 using 4:1:2:3 title "LDLSH" with errorbars ls 4, \
		'' index 1 using 4:1 title '' with linespoints ls 3, \
		'' index 2 using 4:1:2:3 title "Traditional Replicated" with errorbars ls 6, \
		'' index 2 using 4:1 title '' with linespoints ls 5, \
		'' index 3 using 4:1:2:3 title "Traditional External" with errorbars ls 8, \
		'' index 3 using 4:1 title '' with linespoints ls 7

###############################################################################################################################

#### 100a-100b ####
### ThresholdxLatency_100a-100b.pdf ###
set yrange [3:220]
set ytics (5,10,20,30,40,50,70,90,110,150,200, 250)

#set title "Average latency in relation to the similarity threshold for data-set 100a-100b"
set xlabel "Similarity Treshold" 
set ylabel "Average Latency"

set key right top 

set out "TestThresholdxLatency_100a-100b.pdf"
plot 'TestThresholdxLatency_100a-100b.csv' index 0 using 4:1:2:3 title "LDLSH Optimized" with errorbars ls 2, \
		'' index 0 using 4:1 title '' with linespoints ls 1, \
		'' index 1 using 4:1:2:3 title "LDLSH" with errorbars ls 4, \
		'' index 1 using 4:1 title '' with linespoints ls 3, \
		'' index 2 using 4:1:2:3 title "Traditional Replicated" with errorbars ls 6, \
		'' index 2 using 4:1 title '' with linespoints ls 5, \
		'' index 3 using 4:1:2:3 title "Traditional External" with errorbars ls 8, \
		'' index 3 using 4:1 title '' with linespoints ls 7

### TestThresholdxAccuracy_100a-100b.pdf ###
#set yrange [0.93:1]
#set ytics (0.0012,0.0015,0.002,0.0025,0.003)

#set title "Average accuracy in relation to the similarity threshold for data-set 100a-100b"
#set xlabel "Similarity Treshold" 
#set ylabel "Average distance"

#set key right bottom 

#set out "TestThresholdxAccuracy_100a-100b.pdf"
#plot 'TestThresholdxAccuracy_100a-100b.csv' index 0 using 2:1 title "LDLSH Optimized" with linespoints ls 2, \
#		'' index 0 using 2:1 title '' ls 1,\
#		'' index 1 using 2:1 title "LDLSH" with linespoints ls 4, \
#		'' index 1 using 2:1 title '' ls 3, \
#		'' index 2 using 2:1 title "Traditional Replicated" with linespoints ls 6, \
#		'' index 2 using 2:1 title '' ls 5,\
#		'' index 3 using 2:1 title "Traditional External" with linespoints ls 8, \
#		'' index 3 using 2:1 title '' ls 7


### TestThresholdxThroughput_100a-100b.pdf ###
#set yrange [-420:3600]
unset yrange
unset logscale
#set ytics (500,1000,1500,2000,25000,3000,3500)
set ytics 500

#set title "Average throughput in relation to the similarity threshold for data-set 100a-100b"
set xlabel "Similarity Treshold" 
set ylabel "Average Throughput"

set key right bottom 

set out "TestThresholdxThroughput_100a-100b.pdf"
plot 'TestThresholdxThroughput_100a-100b.csv' index 0 using 4:1:2:3 title "LDLSH Optimized" with errorbars ls 2, \
		'' index 0 using 4:1 title '' with linespoints ls 1, \
		'' index 1 using 4:1:2:3 title "LDLSH" with errorbars ls 4, \
		'' index 1 using 4:1 title '' with linespoints ls 3, \
		'' index 2 using 4:1:2:3 title "Traditional Replicated" with errorbars ls 6, \
		'' index 2 using 4:1 title '' with linespoints ls 5, \
		'' index 3 using 4:1:2:3 title "Traditional External" with errorbars ls 8, \
		'' index 3 using 4:1 title '' with linespoints ls 7

###############################################################################################################################

#### 100a-100c ####
### ThresholdxLatency_100a-100c.pdf ###
set yrange [3:220]
set ytics (5,10,20,30,40,50,70,90,110,150,200,250)

#set title "Average latency in relation to the similarity threshold for data-set 100a-100c"
set xlabel "Similarity Treshold" 
set ylabel "Average Latency"

set key right top 

set out "TestThresholdxLatency_100a-100c.pdf"
plot 'TestThresholdxLatency_100a-100c.csv' index 0 using 4:1:2:3 title "LDLSH Optimized" with errorbars ls 2, \
		'' index 0 using 4:1 title '' with linespoints ls 1, \
		'' index 1 using 4:1:2:3 title "LDLSH" with errorbars ls 4, \
		'' index 1 using 4:1 title '' with linespoints ls 3, \
		'' index 2 using 4:1:2:3 title "Traditional Replicated" with errorbars ls 6, \
		'' index 2 using 4:1 title '' with linespoints ls 5, \
		'' index 3 using 4:1:2:3 title "Traditional External" with errorbars ls 8, \
		'' index 3 using 4:1 title '' with linespoints ls 7

### TestThresholdxAccuracy_100a-100c.pdf ###
#set yrange [0.93:1]
#set ytics (0.0012,0.0015,0.002,0.0025,0.003)

#set title "Average accuracy in relation to the similarity threshold for data-set 100a-100c"
#set xlabel "Similarity Treshold" 
#set ylabel "Average distance"

#set key right bottom 

#set out "TestThresholdxAccuracy_100a-100c.pdf"
#plot 'TestThresholdxAccuracy_100a-100c.csv' index 0 using 2:1 title "LDLSH Optimized" with linespoints ls 2, \
#		'' index 0 using 2:1 title '' ls 1,\
#		'' index 1 using 2:1 title "LDLSH" with linespoints ls 4, \
#		'' index 1 using 2:1 title '' ls 3, \
#		'' index 2 using 2:1 title "Traditional Replicated" with linespoints ls 6, \
#		'' index 2 using 2:1 title '' ls 5,\
#		'' index 3 using 2:1 title "Traditional External" with linespoints ls 8, \
#		'' index 3 using 2:1 title '' ls 7


### TestThresholdxThroughput_100a-100c.pdf ###
#set yrange [-420:3600]
unset yrange
unset logscale
#set ytics (500,1000,1500,2000,25000,3000,3500)
set ytics 500

#set title "Average throughput in relation to the similarity threshold for data-set 100a-100c"
set xlabel "Similarity Treshold" 
set ylabel "Average Throughput"

set key right bottom 

set out "TestThresholdxThroughput_100a-100c.pdf"
plot 'TestThresholdxThroughput_100a-100c.csv' index 0 using 4:1:2:3 title "LDLSH Optimized" with errorbars ls 2, \
		'' index 0 using 4:1 title '' with linespoints ls 1, \
		'' index 1 using 4:1:2:3 title "LDLSH" with errorbars ls 4, \
		'' index 1 using 4:1 title '' with linespoints ls 3, \
		'' index 2 using 4:1:2:3 title "Traditional Replicated" with errorbars ls 6, \
		'' index 2 using 4:1 title '' with linespoints ls 5, \
		'' index 3 using 4:1:2:3 title "Traditional External" with errorbars ls 8, \
		'' index 3 using 4:1 title '' with linespoints ls 7

###############################################################################################################################

#set output "ThresholdxLatency.ps"
#pause -1

#set term postscript
#set output "latencies_w.ps"
#replot    
#set term x11
