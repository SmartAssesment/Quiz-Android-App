package com.example.quiz.models;

public class StaticQueue {
    private  int front, rear, capacity;
    private  boolean queue[];

    public StaticQueue(int c)
    {
        front = rear = 0;
        capacity = c;
        queue = new boolean[capacity];
    }

    // function to insert an element
    // at the rear of the queue
    public void queueEnqueue(boolean data)
    {
        // check queue is full or not
        if (capacity == rear) {
            queueDequeue();
        }
        queue[rear] = data;
        rear++;
        return;
    }

    // function to delete an element
    // from the front of the queue
    public void queueDequeue()
    {
        // if queue is empty
        if (front == rear) {
            System.out.printf("\nQueue is empty\n");
            return;
        }

        // shift all the elements from index 2 till rear
        // to the right by one
        else {
            for (int i = 0; i < rear - 1; i++) {
                queue[i] = queue[i + 1];
            }

//            // store 0 at rear indicating there's no element
//            if (rear < capacity)
//                queue[rear] = false;
//
            // decrement rear
            rear = rear - 1;
        }
        return;
    }

    // print queue elements
    public void queueDisplay()
    {
        int i;
        if (front == rear) {
            System.out.printf("\nQueue is Empty\n");
            return;
        }

        // traverse front to rear and print elements
        for (i = front; i < rear; i++) {
            System.out.print(queue[i]+" - ");
        }
        return;
    }

    // print front of queue
    public void queueFront()
    {
        if (front == rear) {
            System.out.printf("\nQueue is Empty\n");
            return;
        }
        System.out.printf("\nFront Element is: %d", queue[front]);
        return;
    }

    public  int getFront() {
        return front;
    }

    public  int getRear() {
        return rear;
    }

    public  int getCapacity() {
        return capacity;
    }

    public  int getCount(){
        int count = 0;
        if(rear !=capacity){
            return -1;
        }
        for (int i = front; i < rear; i++) {
            if(queue[i]){
                count++;
            }

        }
        return count;
    }
}

