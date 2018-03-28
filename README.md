# Metra Schedule Diff Tool

A tool to compare two Metra schedules.  This comes handy when evaluating a proposed schedule against the current schedule.

## Schedule data

Schedule data is stored in the following directory hierarchy: `schedules/<metra line>/<schedule effective date>`.  For example, the schedule for the BSNF line, effective October 9th, 2016, is stored in `schedules/bnsf/2016-10-09`.

The data is stored in a CSV file for each train.  The CSV file is named after the train number.

A list of trains is kept in `schedules/<metra line>/inbound-trains.txt` and `schedules/<metra line>/inbound-trains.txt`. 

## Entering a schedule

Currently, entering a schedule is done by running the entrypoint in `TrainEntry` class.

## Comparing schedules

Comparing a schedule is done by running the entrypoint in the `ScheduleDiff` class.