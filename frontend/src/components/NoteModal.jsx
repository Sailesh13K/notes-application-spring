import React from "react";
import { useEffect } from "react";
import { useState } from "react";

const NoteModal = ({ closeModal, addNote, currNote, editNote }) => {
  const [title, setTitle] = useState("");
  const [description, setDescription] = useState("");

  useEffect(() => {
    if (currNote) {
      setTitle(currNote.title);
      setDescription(currNote.description);
    }
  }, [currNote]);

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (currNote) {
      editNote({ id: currNote.id, title, description });
    } else {
      addNote({ title, description });
    }
    closeModal();
  };
  return (
    <div className="fixed inset-0 bg-gray-800 bg-opacity-75 flex justify-center items-center ">
      <div className="bg-white p-8 rounded shadow-lg w-96">
        <h2 className="text-xl font-bold mb-4">
          {currNote ? "Edit Note" : "Add New Note"}
        </h2>
        <form onSubmit={handleSubmit}>
          <input
            type="text"
            value={title}
            onChange={(e) => setTitle(e.target.value)}
            placeholder="Note Title"
            className="w-full mb-4 p-2 border rounded"
          />
          <textarea
            value={description}
            onChange={(e) => setDescription(e.target.value)}
            placeholder="Note Description"
            className="w-full mb-4 p-2 border rounded"
          />
          <button
            type="submit"
            className="bg-blue-500 text-white px-4 py-2 rounded"
          >
            {currNote ? "Update Note" : "Add Note"}
          </button>
        </form>
        <button className="mt-4 text-red-500" onClick={closeModal}>
          Cancel
        </button>
      </div>
    </div>
  );
};

export default NoteModal;
