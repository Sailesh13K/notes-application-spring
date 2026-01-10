import React, { useEffect, useState } from "react";
import Navbar from "../components/Navbar";
import NoteModal from "../components/NoteModal";
import axios from "axios";
import NoteCard from "../components/NoteCard";
import { toast } from "react-toastify";
import { useAuth } from "../context/ContextProvider.jsx";

function Home() {
  const { user, loading: authLoading } = useAuth();
  const [isModelOpen, setModelOpen] = useState(false);
  const [notes, setNotes] = useState([]);
  const [filteredNotes, setFilteredNotes] = useState([]);
  const [currNote, setCurrNote] = useState(null);
  const [query, setQuery] = useState("");
  const [loading, setLoading] = useState(false);
  const API = import.meta.env.VITE_API_BASE_URL;

  const token = sessionStorage.getItem("token");

  const fetchNotes = async () => {
    if (!token) return;

    try {
      setLoading(true);
      const { data } = await axios.get(`${API}/api/notes`, {
        headers: { Authorization: `Bearer ${token}` },
      });
      setNotes(data.data || data);
      setLoading(false);
    } catch (error) {
      console.log("Error fetching notes:", error.message);
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchNotes();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [token]);

  useEffect(() => {
    setFilteredNotes(
      notes.filter(
        (note) =>
          note.title.toLowerCase().includes(query.toLowerCase()) ||
          note.description.toLowerCase().includes(query.toLowerCase())
      )
    );
  }, [query, notes]);

  const closeModal = () => setModelOpen(false);

  const onEdit = (note) => {
    setCurrNote(note);
    setModelOpen(true);
  };

  const addNote = async ({ title, description }) => {
    try {
      const response = await axios.post(
        `${API}/api/notes/add`,
        { title, description },
        {
          headers: { Authorization: `Bearer ${token}` },
        }
      );
      if (response.data.success) {
        fetchNotes();
        toast.success("Note added successfully");
        closeModal();
      }
    } catch (error) {
      console.error("Error adding note:", error.message);
    }
  };

  const deleteNote = async (id) => {
    try {
      const response = await axios.delete(`${API}/api/notes/${id}`, {
        headers: { Authorization: `Bearer ${token}` },
      });
      if (response.data.success) {
        fetchNotes();
        toast.success("Note deleted successfully");
      }
    } catch (error) {
      console.error("Error deleting note:", error.message);
    }
  };

  const editNote = async ({ id, title, description }) => {
    try {
      const response = await axios.put(
        `${API}/api/notes/${id}`,
        { title, description },
        {
          headers: { Authorization: `Bearer ${token}` },
        }
      );
      if (response.data.success) {
        fetchNotes();
        toast.success("Note updated successfully");
        closeModal();
      }
    } catch (error) {
      console.error("Error editing note:", error.message);
    }
  };
  if (authLoading) {
    return (
      <div className="flex justify-center items-center min-h-screen bg-gray-100">
        <div className="animate-spin rounded-full h-12 w-12 border-t-4 border-b-4 border-teal-500"></div>
      </div>
    );
  }
  return (
    <div className="bg-gray-100 min-h-screen">
      <Navbar setQuery={setQuery} />

      <div className="grid gap-6 sm:grid-cols-2 md:grid-cols-3">
        {user ? (
          loading ? (
            <div className="flex justify-center items-center col-span-full mt-10">
              <div className="animate-spin rounded-full h-12 w-12 border-t-4 border-b-4 border-teal-500"></div>
            </div>
          ) : filteredNotes.length > 0 ? (
            filteredNotes.map((note) => (
              <NoteCard
                key={note.id}
                note={note}
                onEdit={onEdit}
                deleteNote={deleteNote}
              />
            ))
          ) : (
            <p className="m-4 col-span-full text-center text-gray-600">
              No notes found
            </p>
          )
        ) : (
          <div className="flex flex-col items-center justify-center min-h-[70vh] text-center col-span-full">
            <h2 className="text-3xl font-semibold text-gray-800 mb-4">
              Welcome!
            </h2>
            <p className="text-lg text-gray-600 max-w-md">
              Please <span className="text-teal-500 font-medium">login</span> or{" "}
              <span className="text-teal-500 font-medium">signup</span> to view
              and manage your notes efficiently.
            </p>
          </div>
        )}
      </div>

      {token && (
        <button
          onClick={() => {
            setCurrNote(null);
            setModelOpen(true);
          }}
          className="fixed right-6 bottom-6 text-3xl bg-teal-500 hover:bg-teal-600 text-white font-bold p-4 rounded-full shadow-lg transition-transform transform hover:scale-105"
          title="Add Note"
        >
          +
        </button>
      )}

      {isModelOpen && (
        <NoteModal
          closeModal={closeModal}
          addNote={addNote}
          currNote={currNote}
          editNote={editNote}
        />
      )}
    </div>
  );
}

export default Home;
