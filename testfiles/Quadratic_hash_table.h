/*****************************************
 * Instructions
 *  - Replace 'uwuserid' with your uWaterloo User ID
 *  - Select the current calendar term and enter the year
 *  - List students with whom you had discussions and who helped you
 *
 * uWaterloo User ID:  y442zhou@uwaterloo.ca
 * Submitted for ECE 250
 * Department of Electrical and Computer Engineering
 * University of Waterloo
 * Calender Term of Submission:  (Winter 2018)
 *
 * By submitting this file, I affirm that
 * I am the author of all modifications to
 * the provided code.
 *
 * The following is a list of uWaterloo User IDs of those students
 * I had discussions with in preparing this project:
 *    -
 *
 * The following is a list of uWaterloo User IDs of those students
 * who helped me with this project (describe their help; e.g., debugging):
 *    -
 *****************************************/

/*
Some random comments!!!
This is a block of comment although
//
*/

#ifndef QUADRATIC_HASH_TABLE_H
#define QUADRATIC_HASH_TABLE_H

#ifndef nullptr
#define nullptr 0
#endif

#include <iostream>
#include "Exception.h"
// #include "ece250.h"

enum bin_state_t { UNOCCUPIED, OCCUPIED, ERASED };

template <typename Type>
class Quadratic_hash_table {
    // Private member variables
	private:
		int count;
		int power;
		int array_size;
		int mask;
    // variable that stores # of erased bins
        int deleted_bin;
		Type *array;
		bin_state_t *occupied;

		int hash( Type const & ) const;

	public:
		Quadratic_hash_table( int = 5 );
        Quadratic_hash_table(Quadratic_hash_table const &);
		~Quadratic_hash_table();
		int size() const;
		int capacity() const;
		double load_factor() const;
		bool empty() const;
		bool member( Type const & ) const;
		Type bin( int ) const;

		void print() const;

		void insert( Type const & );
		bool erase( Type const & );
		void clear();
        void modifyEntry(Type const &, Type const &);

	// Friends

	template <typename T>
	friend std::ostream &operator<<( std::ostream &, Quadratic_hash_table<T> const & );
};

template <typename Type>
Quadratic_hash_table<Type>::Quadratic_hash_table( int m ):
count( 0 ), power( m ),
array_size( 1 << power ),
mask( array_size - 1 ),
deleted_bin(0),
array( new Type[array_size] ),
occupied( new bin_state_t[array_size] ) {

	for ( int i = 0; i < array_size; ++i ) {
		occupied[i] = UNOCCUPIED;
	}
}

// Copy contructor
template <typename Type>
Quadratic_hash_table<Type>::Quadratic_hash_table( Quadratic_hash_table const & table ):
count( table.count ), power( table.m ),
array_size( table.array_size ),
mask( table.mask ),
deleted_bin(table.deleted_bin),
array( new Type[array_size] ),
occupied( new bin_state_t[array_size] ) {
    // Basically copy everything including the bin status and content in each bin over
    
    for ( int i = 0; i < array_size; ++i ) {
        occupied[i] = table.occupied[i];
        array[i] = table.array[i];
    }
}

// Destructor
template<typename Type>
Quadratic_hash_table<Type>::~Quadratic_hash_table() {
    // delete the two dynamically allocated arrays
    delete[] array;
    delete[] occupied;
}

template<typename Type>
int Quadratic_hash_table<Type>::hash(Type const & obj) const {
    // cast the object to type int
    int key = static_cast<int>(obj);
    // get the initial key
    key = key % array_size;
    
    // if the key is less than 0, add the size to it
    if(key < 0)
        key += array_size;
    return key;
}

// Number of objects in the table
template<typename Type>
int Quadratic_hash_table<Type>::size() const {
    return count;
}

// size of the table
template<typename Type>
int Quadratic_hash_table<Type>::capacity() const {
    return array_size;
}

// Load factor = (# of erased + # of stored) / # of bins
template<typename Type>
double Quadratic_hash_table<Type>::load_factor() const {
    return static_cast<double>((static_cast<double>(count) + deleted_bin) / array_size);
}

// If there is no entry, return true
template<typename Type>
bool Quadratic_hash_table<Type>::empty() const {
    return (count == 0);
}

// Test if the obj is in the table already
template<typename Type>
bool Quadratic_hash_table<Type>::member(Type const & obj) const {
    // If the table is empty, obviously it is not in there
    if(empty())
        return false;
    
    // Get the initial key
    int counter = 0;
    int key = hash(obj);
    // We only loop through the entire table once atmost
    while(counter < array_size){
        // If I find a matched entry and it is occupied, it is found
        if(array[key] == obj && occupied[key] == OCCUPIED)
            return true;
        
        // If not found, get the next key
        counter++;
        key = (key + counter) % array_size;
    }
    // else, it is not there
    return false;
}

// No fancy stuff: just return the corresponding entry in that bin
template<typename Type>
Type Quadratic_hash_table<Type>::bin( int n ) const {
    return array[n];
}

// Insert an object into the table
template<typename Type>
void Quadratic_hash_table<Type>::insert(Type const & obj) {
    // If it is full, throw an exception
    if(count == array_size)
        throw overflow();
    
    // If it is already found, do nothing
    if(member(obj))
        return;
    
    // Get the original key
    int counter = 0;
    int key = hash(obj);
    
    // Find a unused bin
    while(occupied[key] == OCCUPIED){
        counter++;
        key = (key + counter) % array_size;
    }
    
    // If the bin was an erased bin, decrement the number of deleted bins
    if(occupied[key] == ERASED)
        deleted_bin--;
    
    // insert the entry and increment the count
    occupied[key] = OCCUPIED;
    array[key] = obj;
    count++;
}

// erase an obj from the table
template<typename Type>
bool Quadratic_hash_table<Type>::erase(Type const & obj) {
    // If the table is empty, then nothing to delete
    if(empty())
        return false;
    
    // Get the initial key
    int counter = 0;
    int key = hash(obj);
    // Keep looking for the entry and if found, erase it
    while(counter < array_size){
        if(array[key] == obj && occupied[key] == OCCUPIED){
            occupied[key] = ERASED;
            deleted_bin++;
            count--;
            return true;
        }
        counter++;
        key = (key + counter) % array_size;
    }
    // if not found even after looping through the array once, it's not there
    return false;
}

// clear function that return to the original state
template<typename Type>
void Quadratic_hash_table<Type>::modifyEntry(Type const & vertex, Type const & pos) {
    int key = hash(vertex);
    array[key] = pos;
}

// clear function that return to the original state
template<typename Type>
void Quadratic_hash_table<Type>::clear() {
    for(int i = 0; i < array_size; i++){
        // clear all bins
        occupied[i] = UNOCCUPIED;
    }
    // reset everything
    count = 0;
    deleted_bin = 0;
}

// print function that simply outputs the entry of each bin
template<typename Type>
void Quadratic_hash_table<Type>::print() const {
    for(int i = 0; i < array_size; i++)
        std::cout << array[i] << std::endl;
}


template <typename T>
std::ostream &operator<<( std::ostream &out, Quadratic_hash_table<T> const &hash ) {
	for ( int i = 0; i < hash.capacity(); ++i ) {
		if ( hash.occupied[i] == UNOCCUPIED ) {
			out << "- ";
		} else if ( hash.occupied[i] == ERASED ) {
			out << "x ";
		} else {
			out << hash.array[i] << ' ';
		}
	}

	return out;
}

#endif
