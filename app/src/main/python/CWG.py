import numpy as np
import string
from tabulate import tabulate
from random import choices


def board_by_type(_type):
    boards = {
        0: [
            [0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
            [0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
            [0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
            [0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
            [0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
            [0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
            [0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
            [0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
            [0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
            [0, 0, 0, 0, 0, 0, 0, 0, 0, 0]
        ],
        1: [
            [0, 0, 0, 0, 0, 0, 0, 0, 0, 1],
            [1, 0, 1, 0, 1, 0, 1, 0, 1, 0],
            [0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
            [1, 0, 1, 0, 1, 0, 1, 0, 1, 0],
            [0, 1, 0, 1, 0, 0, 0, 0, 0, 0],
            [0, 0, 0, 0, 0, 0, 1, 0, 1, 0],
            [0, 1, 0, 1, 0, 1, 0, 1, 0, 1],
            [0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
            [0, 1, 0, 1, 0, 1, 0, 1, 0, 1],
            [1, 0, 0, 0, 0, 0, 0, 0, 0, 0]
        ],
        2: [
            [0, 0, 0, 0, 0, 1, 0, 0, 0, 0],
            [0, 1, 0, 1, 0, 1, 0, 1, 1, 0],
            [0, 0, 0, 0, 0, 0, 0, 1, 1, 0],
            [0, 1, 0, 1, 0, 1, 0, 1, 1, 0],
            [0, 0, 0, 0, 0, 1, 0, 0, 0, 0],
            [1, 0, 1, 0, 1, 0, 1, 1, 0, 1],
            [1, 0, 1, 0, 1, 0, 0, 0, 0, 0],
            [0, 0, 0, 0, 0, 0, 1, 1, 0, 1],
            [1, 0, 1, 0, 1, 0, 0, 0, 0, 0],
            [1, 0, 1, 0, 1, 0, 1, 1, 0, 1]
        ],
        3: [
            [1, 0, 1, 1, 0, 1, 0, 0, 0, 0],
            [0, 0, 0, 0, 0, 0, 1, 1, 0, 1],
            [1, 0, 1, 1, 0, 1, 0, 0, 0, 0],
            [1, 0, 1, 1, 0, 1, 0, 1, 0, 1],
            [0, 0, 0, 0, 0, 1, 0, 1, 1, 1],
            [1, 1, 1, 0, 1, 0, 0, 0, 0, 0],
            [1, 0, 1, 0, 1, 0, 1, 1, 0, 1],
            [0, 0, 0, 0, 1, 0, 1, 1, 0, 1],
            [1, 0, 1, 1, 0, 0, 0, 0, 0, 0],
            [0, 0, 0, 0, 1, 0, 1, 1, 0, 1]
        ],
        4: [
            [1, 0, 1, 0, 0, 0, 0, 1, 0, 1],
            [0, 0, 0, 0, 1, 1, 0, 0, 0, 0],
            [1, 0, 1, 0, 0, 0, 0, 1, 0, 1],
            [1, 0, 1, 0, 1, 1, 0, 1, 0, 1],
            [0, 0, 0, 0, 1, 1, 0, 0, 0, 0],
            [1, 1, 1, 0, 0, 0, 0, 1, 1, 1],
            [0, 0, 0, 0, 1, 1, 0, 0, 0, 0],
            [0, 1, 0, 1, 1, 1, 1, 0, 1, 0],
            [0, 1, 0, 1, 1, 1, 1, 0, 1, 0],
            [0, 0, 0, 0, 1, 1, 0, 0, 0, 0]
        ],
        5: [
            [1, 0, 1, 0, 1, 0, 1, 0, 0, 0],
            [0, 0, 0, 0, 0, 0, 0, 0, 1, 0],
            [1, 0, 1, 0, 1, 0, 1, 0, 0, 0],
            [0, 0, 0, 0, 0, 0, 0, 1, 0, 1],
            [1, 0, 1, 0, 1, 0, 0, 0, 0, 0],
            [0, 0, 0, 0, 0, 1, 0, 1, 0, 1],
            [1, 0, 1, 0, 0, 0, 0, 0, 0, 0],
            [0, 0, 0, 1, 0, 1, 0, 1, 0, 1],
            [0, 1, 0, 0, 0, 0, 0, 0, 0, 0],
            [0, 0, 0, 1, 0, 1, 0, 1, 0, 1]
        ]
    }
    return boards[_type]


class CrosswordBoard:
    def __init__(self, _layout=0, _size=(10, 10)):
        if _size == (10, 10):
            puzzle = board_by_type(_layout)
            self.size = _size
        else:
            raise Exception("unsupported size")
        self.grid = [[set(string.ascii_lowercase) if elem != 1 else '#' for elem in line] for line in puzzle]
        self.previous_grid = self.grid
        self.clues = {"hor": dict(), "vert": dict()}
        self.word_cells = {"hor": dict(), "vert": dict()}  # hor/vert -> (i,j) -> [cells for that word]
        self.placed_words = []
        self.get_word_cells()

    def __repr__(self):
        table = [[" " if len(elem) > 1 else elem for elem in line] for line in self.grid]
        return tabulate(table, tablefmt="simple_grid")

    def __getitem__(self, item):
        return self.grid.__getitem__(item)

    def get_word_cells(self):
        """
        find all valid start cells and store in self.word_cells
        :return: None
        """
        for i in range(self.size[0]):
            for j in range(self.size[1]):
                if j == 0 and self.grid[i][j] != '#':
                    if (i, j) not in self.word_cells["hor"]:
                        self.word_cells["hor"][(i, j)] = []
                        # self.clues["hor"][(i, j)] = ""
                if j != 0 and self.grid[i][j - 1] == '#' and self.grid[i][j] != '#':
                    if (i, j) not in self.word_cells["hor"]:
                        self.word_cells["hor"][(i, j)] = []
                        # self.clues["hor"][(i, j)] = ""

                if i == 0 and self.grid[i][j] != '#':
                    if (i, j) not in self.word_cells["vert"]:
                        self.word_cells["vert"][(i, j)] = []
                        # self.clues["vert"][(i, j)] = ""
                if i != 0 and self.grid[i - 1][j] == '#' and self.grid[i][j] != '#':
                    if (i, j) not in self.word_cells["vert"]:
                        self.word_cells["vert"][(i, j)] = []
                        # self.clues["vert"][(i, j)] = ""
        short_words = []
        for cell in self.word_cells["hor"].keys():
            cur_i, cur_j = cell
            while cur_j < self.size[1] and self.grid[cur_i][cur_j] != '#':
                self.word_cells["hor"][cell].append((cur_i, cur_j))
                cur_j += 1
            if len(self.word_cells["hor"][cell]) in [1, 2]:
                short_words.append(cell)

        for word in short_words:
            del self.word_cells["hor"][word]
        short_words = []
        for cell in self.word_cells["vert"].keys():
            cur_i, cur_j = cell
            while cur_i < self.size[1] and self.grid[cur_i][cur_j] != '#':
                self.word_cells["vert"][cell].append((cur_i, cur_j))
                cur_i += 1
            if len(self.word_cells["vert"][cell]) in [1, 2]:
                short_words.append(cell)
        for word in short_words:
            del self.word_cells["vert"][word]

    def get_start(self, i, j):
        """
        :i: int between 0 and size[0]-1
        :j: int between 0 and size[1]-1
        :return: start cell for cell with coords (i,j)
        """
        hor, vert = None, None
        for key, value in self.word_cells["hor"].items():
            if (i, j) in value:
                hor = key
                break
        for key, value in self.word_cells["vert"].items():
            if (i, j) in value:
                vert = key
                break
        return hor, vert

    def get_possible_words(self, start_cell, direction, vocab):
        """
        :param start_cell: tuple (i,j) where i is row and j is column of start cell
        :param direction: "hor" or "vert", determines direction of start cell
        :param vocab: vocabulary of words, dict
        :return: list of words that could fit this start cell considering possible letters
        """
        words_sets = []
        setup = [self.grid[i][j] for (i, j) in self.word_cells[direction][start_cell]]
        for i, ch_dict in vocab[len(setup)].items():  # pos
            pos_words = set()
            for ch, words in ch_dict.items():  # ch
                if ch in setup[i]:
                    pos_words = pos_words | words
                # print(pos_words)
            words_sets.append(pos_words)
        if len(words_sets):
            return sorted(list((words_sets[0].intersection(*words_sets) - set(self.placed_words))))
        else:
            return []

    def refresh_letters(self, start_cell, direction, vocab):
        """
        calculates possible letters for cells of given start cell and refreshes board
        :param start_cell: tuple (i,j) where i is row and j is column of start cell
        :param direction: "hor" or "vert", determines direction of start cell
        :param vocab: vocabulary of words, dict
        :return: None
        """
        words = self.get_possible_words(start_cell, direction, vocab)
        cells = self.word_cells[direction][start_cell]
        for i in range(len(cells)):
            letters = set(np.unique([elem[i] for elem in words]))
            cur_cell = self.grid[cells[i][0]][cells[i][1]]
            if direction == 'hor':
                if isinstance(cur_cell, set):
                    self.grid[cells[i][0]][cells[i][1]] = letters
            else:
                if isinstance(cur_cell, set):
                    self.grid[cells[i][0]][cells[i][1]] |= letters

    def refresh_all_letters(self, vocab):
        """
        calculates possible letters for all cells on the board
        :param vocab: vocabulary of words, dict
        :return: None
        """
        for start in self.word_cells["hor"]:
            self.refresh_letters(start, "hor", vocab)
        for start in self.word_cells["vert"]:
            self.refresh_letters(start, "vert", vocab)

    def place_word(self, start_cell, direction, word):
        """
        :param start_cell:  tuple (i,j) where i is row and j is column of start cell
        :param direction: "hor" or "vert", determines direction of start cell
        :param word: str, word to place in cells according to given start cell
        :return: None
        """
        self.previous_grid = self.grid
        self.placed_words.append(word)
        coords = self.word_cells[direction][start_cell]
        for i in range(len(word)):
            self.grid[coords[i][0]][coords[i][1]] = word[i]

    def step_back(self):
        """
        returns board to its previous state (before last word was placed)
        :return: None
        """
        self.grid = self.previous_grid
        self.placed_words.pop(-1)

    def is_placed(self, start_cell, direction):
        """
        :param start_cell: tuple (i,j) where i is row and j is column of start cell
        :param direction: "hor" or "vert", determines direction of start cell
        :return: True if all cells for this start cell are filled with letters else False
        """
        setup = [self.grid[i][j] for (i, j) in self.word_cells[direction][start_cell]]
        for ch in setup:
            if len(ch) > 1:
                return False
        return True

    def is_finished(self):
        """
        :return: True if all words are placed else False
        """
        return len(self.placed_words) == (len(self.word_cells["hor"].keys()) + len(self.word_cells["vert"].keys()))

    def restart(self):
        """
        returns board into initialized state
        :return:None
        """
        self.grid = [[set(string.ascii_lowercase) if elem != '#' else elem for elem in line] for line in self.grid]
        self.previous_grid = self.grid
        self.placed_words = []

    def fill_clues(self, dictionary):
        """
        for each placed word on board fills in clues fro given dictionary
        :param dictionary:
        :return: None
        """
        weights = {"noun":2, "verb":2,"adverb":3,"adjective":1,"interjection":1,"pronoun":3,"preposition":1,"conjunction":1,"numeral":1}
        for cell in self.word_cells["hor"].keys():
            word = "".join([self.grid[i][j] for (i, j) in self.word_cells["hor"][cell]])
            self.clues["hor"][cell] = choices(list(dictionary[word].values()),weights=[weights[i] for i in dictionary[word].keys()],k=1)
        for cell in self.word_cells["vert"].keys():
            word = "".join([self.grid[i][j] for (i, j) in self.word_cells["vert"][cell]])
            self.clues["vert"][cell] = choices(list(dictionary[word].values()),weights=[weights[i] for i in dictionary[word].keys()],k=1)


class CrosswordGenerator:
    def __init__(self, _vocab, _dictionary, _learned_words, _size=(10, 10), _layout=1):
        self.size = _size
        self.vocab = _vocab
        self.dictionary = _dictionary
        self.learned_words = _learned_words
        self.board = CrosswordBoard(_layout, _size)

    def show(self):
        print(self.board)

    def choose_next_start(self):
        """
        chooses next best cells to fill
        :return: start cell (i,j), direction ('hor' or 'vert')
        """
        words_num = {"hor": dict(), "vert": dict()}
        for hor_start in self.board.word_cells["hor"].keys():
            words_num["hor"][hor_start] = len(self.board.get_possible_words(hor_start, "hor", self.vocab))
        for vert_start in self.board.word_cells["vert"].keys():
            words_num["vert"][vert_start] = len(self.board.get_possible_words(vert_start, "vert", self.vocab))

        hor_min = min(words_num["hor"], key=words_num["hor"].get)
        vert_min = min(words_num["vert"], key=words_num["vert"].get)
        minimal, direction = (hor_min, "hor") if words_num["hor"][hor_min] <= words_num["vert"][vert_min] else (
        vert_min, "vert")
        while self.board.is_placed(minimal, direction):
            words_num[direction][minimal] = 100000
            hor_min = min(words_num["hor"], key=words_num["hor"].get)
            vert_min = min(words_num["vert"], key=words_num["vert"].get)
            minimal, direction = (hor_min, "hor") if words_num["hor"][hor_min] <= words_num["vert"][vert_min] else (
                vert_min, "vert")
        return minimal, direction

    def calc_word_value(self, word):
        """
        calculate word value by letters usage frequency
        :param word: str
        :return: float
        """
        if word in self.learned_words:
            return 0.1
        letter_values = {"q": 1, "z": 1, "x": 1, "j": 1, "k": 2, "v": 2, "b": 2, "w": 2, "p": 2, "y": 3, "g": 3, "u": 3,
                         "m": 3, "c": 4, "f": 4,
                         "l": 4, "d": 4, "h": 4, "s": 5, "i": 5, "r": 5, "n": 5, "o": 6, "a": 6, "t": 6, "e": 6}
        value = 0
        for ch in word:
            value += letter_values[ch]
        return value / len(word)

    def choose_best_word(self, possible_words):
        """
        :param possible_words: iterable of words
        :return: str, best word out of possible according to its value
        """
        values = [self.calc_word_value(word) for word in possible_words]
        return choices(possible_words, weights=values, k=1)[0]

    def generate(self):
        """
        fills board and clues, calls inner generate when it reaches a dead end
        :return: board
        """
        while self.inner_generate() == "RESTART":
            self.board.restart()
            print("RESTART")
        self.board.fill_clues(self.dictionary)
        return self.board

    def inner_generate(self):
        """
        main loop (chooses best start cell, best word, if cant place any words returns to previous iteration)
        :return: None
        """
        prev_possible_words = []
        prev_start, prev_dir = None, None
        while not self.board.is_finished():
            # print("placed: ",self.board.placed_words)

            start, direction = self.choose_next_start()
            # print("next: ", start, direction)
            possible_words = self.board.get_possible_words(start, direction, self.vocab)
            # print("pos words: ",possible_words)
            while len(possible_words) == 0:
                if len(prev_possible_words) == 0:
                    return "RESTART"
                self.board.step_back()
                best_word = self.choose_best_word(prev_possible_words)
                prev_possible_words.remove(best_word)

                self.board.place_word(prev_start, prev_dir, best_word)
                possible_words = self.board.get_possible_words(start, direction, self.vocab)

            best_word = self.choose_best_word(possible_words)
            possible_words.remove(best_word)
            self.board.place_word(start, direction, best_word)

            prev_start, prev_dir = start, direction
            prev_possible_words = possible_words.copy()
        return None
