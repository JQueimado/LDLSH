#!/usr/bin/gnuplot
reset
#set fontsize = 100
#set term postscript enhanced eps fontsize solid

#set autoscale
##set title "Percentage of Sensitive Reads"
# set size ratio 0.65

# set lmargin 0.3
# set rmargin 0.3
set bmargin 3

A = "#2F528F"
B = "#ED7D31"
C = "#000000"

#A = "#222222"
#B = "#444444"
#C = "#666666"
#D = "#888888"
#E = "#AAAAAA"
#F = "#CCCCCC"

#set ytics 0.01,0.1,1
#set logscale x 2
#set logscale y
#set logscale y2
#set boxwidth 0.05
#set style fill solid
#set yrange [0:14982406523]
#f(x) = exp(-x**2 / 2)
#plot [t=-4:4] f(t), t**2 / 16
#set ytics 0,10,100 nomirror
#set ytics ("1%%" 1, "10%%" 10, "100%%" 100)
#set y2tics ("2.5%%" 2.5, "5%%" 5, "7.5%%" 7.5, "10%%" 10, "20%%" 20)
#set ytics (250, 500, 1000, 1500, 2000, 2500) nomirror
#set ytics ("0.4" 0.4,"0.5" 0.5, 1,2,4,8,16) nomirror
#set xtics out nomirror
#set ytics nomirror 
#set grid ytics

set style line 1 lt rgb A lw 2 pt 9 ps 1 pi -2
set style line 2 lt rgb C lw 2 pt 1 ps 2 pi -2
set style line 3 lt rgb B lw 2 pt 7 ps 1 pi -2
set style line 9 lt rgb C lw 10 dt 2
set style line 10 lt rgb C lw 10 dt 10
set style line 11 lt rgb "#000" lw 3

set boxwidth 0.5
set style fill solid

set xtics ("LDLSH\nOptimized" .25,\
	"LDLSH" 1.75,\
	"Traditional\nExternal" 3.25,\
	"Traditional\nReplicated" 4.75)

set yrange [0.785:1.14]
set ytics (.8, .85, .9, .95, 1)

##set style line 6 lt rgb B lw 1 pt 6 pi -1 ps 1
#set style line 7 lt rgb C lw 4 pt 8 pi -1 ps 1
#set style line 8 lt rgb D lw 4 pt 6 pi -1 ps 1
#set style line 9 lt rgb E lw 4 pt 4 pi -1 ps 1
##set style line 10 lt rgb F lw 1 pt 6 pi -1 ps 1
#set style line 11 lt rgb G lw 4 pt 7 ps 1

#set style line 5 lt rgb A lw 4 pt 6 pi -1 ps 2
#set style line 11 lt rgb G lw 4 pt 7 ps 2

#set style line 6 lt rgb A lw 2 pt 6 pi -1 ps 2
#set style line 12 lt rgb G lw 2 pt 7 ps 2

set key right top 
##set key vertical maxrows 3 left
#set format y "%.0s"	
#set format x "%g%%"
#set linetype cycle 1
#set linetype cycle 2
#set linetype cycle 3
#set linetype cycle 4

set terminal pdf color font ",60" size 18, 10
set size ratio .6 1,1

###############################################################################################################################

##### TEST 10_10 #####

### ThresholdxLatency_10a-10a.pdf ###

#set xrange [-0.5:5.5]

#set ylabel "Average Latency with error"

#set out "TestAccuracyxNgram_10a-10b.pdf"
#plot 'TestAccuracyxNgram_10a-10b.csv' every 2 using 1:3 title 'Ngram 3' with boxes ls 1,\
#	'TestAccuracyxNgram_10a-10b.csv' every 2 using 1:3:4:5 title '' with errorbars ls 2,\
#	'TestAccuracyxNgram_10a-10b.csv' every 2::1 using 1:3 title 'Ngram 4' with boxes ls 3,\
#	'TestAccuracyxNgram_10a-10b.csv' every 2::1 using 1:3:4:5 title '' with errorbars ls 2,\
#	0.7937854073276378	title 'dataset similarity' ls 9,\
#	0.8495047623629264	title 'dataset similarity' ls 10


set xrange [-0.5:5.5]

set ylabel "Average distance"

set out "TestAccuracyxNgram_10.pdf"
plot 'TestAccuracyxNgram_10.csv' every 2 using 1:3 title 'Ngram 3' with boxes ls 1,\
	'TestAccuracyxNgram_10.csv' every 2 using 1:3:4:5 title '' with errorbars ls 2,\
	'TestAccuracyxNgram_10.csv' every 2::1 using 1:3 title 'Ngram 4' with boxes ls 3,\
	'TestAccuracyxNgram_10.csv' every 2::1 using 1:3:4:5 title '' with errorbars ls 2,\
	0.7937854073276378	title 'base for Ngram 3' ls 9,\
	0.8495047623629264	title 'base for Ngram 4' ls 10,\
	1 title '' ls 11

set out "TestAccuracyxNgram_10x10.pdf"
plot 'TestAccuracyxNgram_10x10.csv' every 2 using 1:3 title 'Ngram 3' with boxes ls 1,\
	'TestAccuracyxNgram_10x10.csv' every 2 using 1:3:4:5 title '' with errorbars ls 2,\
	'TestAccuracyxNgram_10x10.csv' every 2::1 using 1:3 title 'Ngram 4' with boxes ls 3,\
	'TestAccuracyxNgram_10x10.csv' every 2::1 using 1:3:4:5 title '' with errorbars ls 2,\
	0.8470108839664949	title 'base for Ngram 3' ls 9,\
	0.9138432553377478	title 'base for Ngram 4' ls 10,\
	1 title '' ls 11