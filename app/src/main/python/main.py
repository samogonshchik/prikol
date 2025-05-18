import CWG
from time import time
import json
import csv
import string
from os.path import dirname, join


# reading from json and csv and preparing dicts for CrosswordGenerator constructor
with open(join(dirname(__file__), "word_definitions.json"),'r') as f:
    all_words = json.load(f)
words = {i:{j:{ch:set() for ch in set(string.ascii_lowercase)} for j in range(0,i)} for i in range(1,18)}

for word in all_words.keys():
    for i in range(len(word)):
        words[len(word)][i][word[i]].add(word)

with open(join(dirname(__file__), "learned.csv"),'r') as f:
    reader = csv.reader(f, delimiter=',')
    try:
        learned_words = next(reader)
    except StopIteration:
        learned_words = []

# generating crossword
cw_gen = CWG.CrosswordGenerator(_vocab=words, _dictionary=all_words, _learned_words=learned_words, _layout=1)
cw_gen.generate()
board = cw_gen.board
# board's main attributes are [grid, clues, word_cells, placed_words, size]
# both clues and word_cells split into 'hor' and 'vert', and store words start cells as keys

grid = board.grid
clues = board.clues
word_cells = board.word_cells
placed_words = board.placed_words
