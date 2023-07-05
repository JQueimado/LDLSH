set yrange [0:*]      # start at zero, find max from the data
set style fill solid  # solid color boxes
unset key             # turn off all titles

set style data histograms
set style histogram rowstacked
set boxwidth 0.9
set key noinvert box
set tics scale 0.0
set style fill solid 1.0 border -1
set yrange [0:*]
set xtics norotate nomirror

set terminal pdf size 18, 10 font ",50"
set out "candidates_sizes.pdf"

plot "candidates_sizes.csv" using 2 t "var1", \
    '' using 3:xticlabels(1) t "Var 2", \
    '' using 4 t "var", \
    '' using 5 t "meh"